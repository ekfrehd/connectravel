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

    // 검색 조건이 없는 게시판
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

    // 흔한 전체 게시판 출력 방식
    // searchPage 메서드는 흔한 전체 게시판 출력 방식을 사용하여 게시물을 검색하고 페이지 처리를 수행
    // type: 검색 조건 (제목, 작성자, 내용)
    // keyword: 검색어
    // pageable: 페이지 요청 정보 (페이지 번호, 페이지 크기, 정렬 방식 등)
    @Override
    public Page<Object[]> searchPage(String[] type, String keyword, Pageable pageable) {
        // 1. 테이블 세팅
        QQnaBoard qnaBoard = QQnaBoard.qnaBoard;
        QQnaReply reply = QQnaReply.qnaReply;
        QMember member = QMember.member;

        // 1행 : qnaBoard를 기반으로 한 JPQLQuery 객체를 생성. 결과로 반환되는 튜플을 포함하는 쿼리를 정의하고 있음.
        // 2행 : qnaBoard와 member 사이의 왼쪽 외부 조인을 수행함.
        // 조인 조건은 qnaBoard.member.eq(member)로, qnaBoard의 member 필드가 member 엔티티와 같아야 함.
        // 3행 : reply와 qnaBoard 사이의 왼쪽 외부 조인을 수행함.
        // 조인 조건은 reply.qnaBoard.eq(qnaBoard)로, reply의 qnaBoard 필드가 qnaBoard 엔티티와 같아야 함.
        // 4행 : 조회할 컬럼을 설정함. 여기서는 qnaBoard, member, 그리고 reply의 개수를 선택함. 결과로 반환되는 튜플에 이 정보가 포함됨.
        // 5행 : 조회 조건을 설정. 여기서는 qnaBoard의 bno 필드 값이 0보다 큰 게시물만 선택하도록 조건을 설정.
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
            // qnaBoard.bno.gt(0L) : QnaBoard의 bno 필드 값이 0보다 큰 경우만 결과를 반환
            // .andAnyOf(conditions) : 주어진 conditions 배열의 요소 중 하나라도 참인 경우 결과를 반환
            // 예를 들어, type 배열에 't'와 'c'가 포함되어 있고 키워드가 'hello'인 경우, conditions 배열은 다음과 같이 결과가 나옴.
            //conditions[0] = qnaBoard.title.contains('hello')
            //conditions[1] = qnaBoard.content.contains('hello')
            tuple.where(qnaBoard.bno.gt(0L).andAnyOf(conditions));
        }

        // order by : Sort 사용
        // 리스트가 한줄씩 나오므로 stream 사용. 그걸 반복(foreach)
        // 방향을 오름차순 할꺼야? : 내림차순 할꺼야?
        // prop 으로 차순 설정 하는거
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            tuple.orderBy(new OrderSpecifier(direction, ExpressionUtils.path(Object.class, qnaBoard, prop)));
        });

        // 결과를 그룹화하기 위해 qnaBoard를 기준으로 groupBy 절을 추가함.
        // 이렇게 하여 동일한 게시물이 여러 번 반환되지 않도록 해야됨(예: 한 사람이 한 게시물에 여러 댓글을 쓴 경우.)
        tuple.groupBy(qnaBoard);

        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }

    // 1:1문의 형 페이지 리스트 출력 시키는거
    // 문제점 : 로그인 안 한사람은 member가 null이라 예외처리됨
    /*@Override
    public Page<Object[]> searchPageByWriter(String[] type, Member writer, String keyword, Pageable pageable) {
        QQnaBoard qnaBoard = QQnaBoard.qnaBoard;
        QQnaReply reply = QQnaReply.qnaReply;
        QMember member = QMember.member;

        // 기존 쿼리문에서
        JPQLQuery<QnaBoard> jpqlQuery = from(qnaBoard);
        jpqlQuery.leftJoin(member).on(qnaBoard.member.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.qnaBoard.eq(qnaBoard));

        // where 절이 추가된거라고 생각하면 됨
        // where qnaBoard.member_id = :writer_id
        JPQLQuery<Tuple> tuple = jpqlQuery.select(qnaBoard, member, reply.count());
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qnaBoard.member.eq(writer));

        if (type != null) {
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : type) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(qnaBoard.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.nickName.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(qnaBoard.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        // order by
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            tuple.orderBy(new OrderSpecifier(direction, ExpressionUtils.path(Object.class, qnaBoard, prop)));
        });

        tuple.groupBy(qnaBoard);

        // page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();
        long count = tuple.fetchCount();

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }*/

    @Override
    public Page<Object[]> searchPageAdminBaord(String[] type, String category, String keyword, Pageable pageable) {

        // 1. 테이블 세팅
        QAdminBoard adminBoard = QAdminBoard.adminBoard;
        QAdminReply adminReply = QAdminReply.adminReply;
        QMember member = QMember.member;

        // 1행 : adminBoard를 기반으로 한 JPQLQuery 객체를 생성. 결과로 반환되는 튜플을 포함하는 쿼리를 정의하고 있음.
        // 2행 : adminBoard와 member 사이의 (A U ( A n B)) 집합 수행
        // 조인 조건은 adminBoard.member.eq(member)로, adminBoard의 member 필드가 member 엔티티와 같아야 함.
        // 3행 : adminReply와 adminBoard 사이의 (A U ( A n B) 집합 수행.
        // 조인 조건은 adminReply.adminBoard.eq(adminBoard)로, adminReply의 adminBoard 필드가 adminBoard 엔티티와 같아야 함.
        // 4행 : 조회할 컬럼을 설정함. 여기서는 adminBoard, member, 그리고 adminReply의 개수를 선택함. 결과로 반환되는 튜플에 이 정보가 포함됨.
        // 5행 : 조회 조건을 설정. 여기서는 adminBoard의 bno 필드 값이 0보다 큰 게시물만 선택하도록 조건을 설정.
        JPQLQuery<Tuple> tuple = from(adminBoard).leftJoin(member).on(adminBoard.member.eq(member)).leftJoin(adminReply).on(adminReply.adminBoard.eq(adminBoard)).select(adminBoard, member, adminReply.count()).where(adminBoard.bno.gt(0L));

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

    /*@Override
    public Page<Object[]> searchPageAccommodation(String[] type, String keyword, String category, String region, LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice, Pageable pageable) {
        QAccommodation accommodation = QAccommodation.accommodation;
        QRoom room = QRoom.room;
        QReservation reservationEntity = QReservation.reservation;


        JPQLQuery<Tuple> query = from(accommodation).select(accommodation, room, room.price.min()).join(room).on(accommodation.eq(room.accommodation)).leftJoin(reservationEntity).on(room.eq(reservationEntity.room).and(reservationEntity.startDate.lt(endDate)).and(reservationEntity.endDate.gt(startDate)).and(reservationEntity.state.isTrue())).where(reservationEntity.rvno.isNull().or(reservationEntity.room.isNull())).groupBy(accommodation);


        // 키워드 검색 하는 곳 (검색 파라미터 삽입하는 곳)
        if (type != null || region != null || category != null || keyword != null || inputedMinPrice != null || inputedMaxPrice != null) {
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            if (type != null) {
                for (String t : type) {
                    switch (t) {
                        case "c":
                            conditionBuilder.or(accommodation.category.contains(keyword));
                            break;
                        case "r":
                            conditionBuilder.or(accommodation.region.contains(keyword));
                            break;
                    }
                }
            }

            if (region != null) {
                conditionBuilder.or(accommodation.region.contains(region));
            }
            if (category != null) {
                conditionBuilder.or(accommodation.category.contains(category));
            }
            if (keyword != null) {
                conditionBuilder.or(accommodation.name.contains(keyword).or(accommodation.address.contains(keyword)).or(accommodation.category.contains(keyword)).or(accommodation.region.contains(keyword)));
            }

            if (inputedMinPrice != null && inputedMaxPrice != null) {
                //BolleanExpress 조건을 찾는 클래스 (가격 조건용)
                BooleanExpression priceCondition = room.price.between(inputedMinPrice, inputedMaxPrice);
                // 조건을 먼저 세팅한다음에 --> 검색 조건에 추가
                conditionBuilder.and(priceCondition);
            }

            query.where(conditionBuilder);
        }

        // order by
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            query.orderBy(new OrderSpecifier(direction, ExpressionUtils.path(Object.class, accommodation, prop)));
        });

        // page 처리
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        List<Tuple> tuples = query.fetch();
        long count = query.fetchCount();

        List<Object[]> result = tuples.stream().map(tuple -> new Object[]{tuple.get(accommodation), tuple.get(room), tuple.get(room.price.min()), tuple.get(reservationEntity.rvno)}).collect(Collectors.toList());

        return new PageImpl<>(result, pageable, count);
    }*/

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
}
