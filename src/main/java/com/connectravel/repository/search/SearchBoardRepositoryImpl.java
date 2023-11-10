package com.connectravel.repository.search;

import com.connectravel.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(QnaBoard.class);
    }

/*    // 검색 조건이 없는 게시판
    @Override
    public QnaBoard search1() {
        log.info("search1.....");

        QQnaBoard qnaBoard = QQnaBoard.qnaBoard;
        QQnaReply reply = QQnaReply.qnaReply;
        QMember member = QMember.member;

        JPQLQuery<Tuple> tuple = from(qnaBoard).leftJoin(member).on(qnaBoard.member.eq(member)).leftJoin(reply).on(reply.qnaBoard.eq(qnaBoard)).select(qnaBoard, member.email, reply.count()).groupBy(qnaBoard);

        List<Tuple> result = tuple.fetch();

        return null;
    }
*/


    @Override
    public Page<Object[]> searchPage(String[] type, String keyword, Pageable pageable) {
        QQnaBoard qnaBoard = QQnaBoard.qnaBoard;
        QQnaReply reply = QQnaReply.qnaReply;
        QMember member = QMember.member;

        JPQLQuery<Tuple> tuple = from(qnaBoard).leftJoin(member).on(qnaBoard.member.eq(member)).leftJoin(reply).on(reply.qnaBoard.eq(qnaBoard)).select(qnaBoard, member, reply.count()).where(qnaBoard.bno.gt(0L));

        // 검색 조건을 설정함. 검색 조건에 따라 BooleanExpression 객체를 생성하여 배열에 저장
        if (type != null) {
            BooleanExpression[] conditions = new BooleanExpression[type.length];
            // ex {[t],[w],[]} // 두개의 결과 출력
            for (int i = 0; i < type.length; i++) {
                switch (type[i]) {
                    case "t":
                        conditions[i] = qnaBoard.title.contains(keyword);
                        break;
                    case "w":
                        conditions[i] = member.nickName.contains(keyword);
                        break;
                    case "c":
                        conditions[i] = qnaBoard.content.contains(keyword);
                        break;
                }
            }

            tuple.where(qnaBoard.bno.gt(0L).andAnyOf(conditions));
        }

        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            tuple.orderBy(new OrderSpecifier(direction, ExpressionUtils.path(Object.class, qnaBoard, prop)));
        });

        tuple.groupBy(qnaBoard);

        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }

    @Override
    public Page<Object[]> searchPageAdminBoard(String[] type, String category, String keyword, Pageable pageable) {

        // 1. 테이블 세팅
        QAdminBoard adminBoard = QAdminBoard.adminBoard;
        QAdminReply adminReply = QAdminReply.adminReply;
        QMember member = QMember.member;

        JPQLQuery<Tuple> tuple =
                from(adminBoard)
                        .leftJoin(member)
                        .on(adminBoard.member.eq(member))
                        .leftJoin(adminReply)
                        .on(adminReply.adminBoard.eq(adminBoard))
                        .select(adminBoard, member, adminReply.count())
                        .where(adminBoard.abno.gt(0L));

        // 검색 조건을 설정함. 검색 조건에 따라 BooleanExpression 객체를 생성하여 배열에 저장
        // 키워드 검색 하는 곳 (검색 파라미터 삽입하는 곳)
        if (type != null || keyword != null || category != null) {
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            if (type != null) {
                for (String t : type) {
                    switch (t) {
                        case "t":
                            conditionBuilder.or(adminBoard.title.contains(keyword));
                            break;
                        case "c":
                            conditionBuilder.or(adminBoard.content.contains(keyword));
                            break;
                    }
                }
            }

            if (category != null) {
                conditionBuilder.or(adminBoard.category.contains(category));
            }

            tuple.where(conditionBuilder);
        }

        // order by : Sort 사용
        // 리스트가 한줄씩 나오므로 stream 사용. 그걸 반복(foreach)
        // 방향을 오름차순 할꺼야? : 내림차순 할꺼야?
        // prop 으로 차순 설정 하는거
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            tuple.orderBy(new OrderSpecifier(direction, ExpressionUtils.path(Object.class, adminBoard, prop)));
        });

        // 결과를 그룹화하기 위해 qnaBoard를 기준으로 groupBy 절을 추가함.
        // 이렇게 하여 동일한 게시물이 여러 번 반환되지 않도록 해야됨(예: 한 사람이 한 게시물에 여러 댓글을 쓴 경우.)
        tuple.groupBy(adminBoard);

        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }


/*
    @Override
    public Page<Object[]> searchTourBoard(String[] type, String keyword, String category, String region, Pageable pageable) {

        QTourBoard qTourBoard = QTourBoard.tourBoard;

        JPQLQuery<TourBoard> tuple = from(qTourBoard).select(qTourBoard).where(qTourBoard.tbno.gt(0L));


        //검색 조건을 작성하기
        if (type != null || region != null || category != null || keyword != null) {
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            if (type != null) {
                for (String t : type) {
                    switch (t) {
                        case "c":
                            conditionBuilder.or(qTourBoard.content.contains(keyword));
                            break;
                        case "t":
                            conditionBuilder.or(qTourBoard.title.contains(keyword));
                            break;
                        case "r":
                            conditionBuilder.or(qTourBoard.region.contains(keyword));
                            break;
                    }
                }
            }

            if (region != null) {
                conditionBuilder.or(qTourBoard.region.contains(region));
            }
            if (category != null) {
                conditionBuilder.or(qTourBoard.category.contains(category));
            }
            if (type == null && keyword != null) {
                conditionBuilder.or(qTourBoard.region.contains(keyword).or(qTourBoard.content.contains(keyword)).or(qTourBoard.address.contains(keyword)).or(qTourBoard.category.contains(keyword)).or(qTourBoard.title.contains(keyword)));
            }
            tuple.where(conditionBuilder);
        }

        // order by
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            tuple.orderBy(new OrderSpecifier(direction, ExpressionUtils.path(Object.class, qTourBoard, prop)));
        });

        tuple.groupBy(qTourBoard);

        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<TourBoard> result = tuple.fetch();

        long count = tuple.fetchCount();

        return new PageImpl<>(result.stream().map(t -> new Object[]{t}).collect(Collectors.toList()), pageable, count);
    }
*/
}
