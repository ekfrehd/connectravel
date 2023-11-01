package com.connectravel.repository;

import com.connectravel.entity.AdminBoard;
import com.connectravel.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface AdminBoardRepository extends JpaRepository<AdminBoard, Long>, QuerydslPredicateExecutor<AdminBoard>, SearchBoardRepository {

    // bno 값에 해당하는 QnaBoard 게시물, 해당 게시물을 작성한 회원(Member) 객체, 그리고 해당 게시물에 달린 AdminReply 댓글의 개수를 가져옴
    // select b, w, count(r) - AdminBoard 객체(b), 회원 객체(w), 그리고 댓글 개수(count(r))를 선택함.
    // from AdminBoard b - AdminBoard 엔티티를 대상으로 쿼리를 실행하며, 이를 b로 별칭(alias)으로 함.
    // left join b.member w - AdminBoard 객체와 Member 객체를 연관 관계(member 필드를 통해)로 왼쪽 조인(left join)함. Member 객체는 w로 별칭함.
    // left outer join AdminReply r on r.adminBoard = b - AdminBoard 객체와 AdminReply 객체를 연관 관계(adminBoard 필드를 통해)로 왼쪽 외부 조인(left outer join)함. AdminReply 객체는 r로 별칭으로 함.
    // where b.bno = :bno - AdminBoard의 bno 필드 값이 주어진 bno 파라미터 값과 같은 경우에만 결과를 반환.
    // 이 쿼리를 사용하면 특정 관리자 게시물의 정보와 작성자 정보, 그리고 댓글 개수를 한 번의 쿼리로 가져올 수 있음! 이렇게 가져온 데이터는 Object 타입으로 반환되며, 필요한 경우 적절한 타입으로 캐스팅하여 사용할 수 있음!
    @Query("select b, w, count(r) from AdminBoard b left join b.member w left outer join AdminReply r on r.adminBoard = b where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
