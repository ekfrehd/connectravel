package com.connectravel.repository;

import com.connectravel.domain.entity.AdminBoard;
import com.connectravel.domain.entity.AdminBoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminBoardImgRepository extends JpaRepository<AdminBoardImg, Long> {

    @Query("SELECT i from AdminBoardImg i where i.adminBoard = :abno")
    List<AdminBoardImg> getImgByAbno(@Param("abno") AdminBoard abno);

}