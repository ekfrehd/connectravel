package com.connectravel.repository;

import com.connectravel.domain.entity.AdminBoard;
import com.connectravel.domain.entity.AdminReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminReplyRepository extends JpaRepository<AdminReply, Long> {

    @Modifying
    @Query("delete from AdminReply r where r.adminBoard.abno =:abno") // 해당되는 게시물의 번호의 댓글을 삭제한다.
    void deleteByAbno(Long abno);

    List<AdminReply> getRepliesByAdminBoardOrderByArno(AdminBoard adminBoard); // 게시물로 댓글 목록 가져오기

}