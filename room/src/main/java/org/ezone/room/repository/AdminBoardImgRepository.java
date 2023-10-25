package org.ezone.room.repository;

import org.ezone.room.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminBoardImgRepository extends JpaRepository<AdminBoardImgEntity, Long> {
    @Query("SELECT i from AdminBoardImgEntity i where i.adminBoard = :bno")
    List<AdminBoardImgEntity> GetImgbybno(@Param("bno") AdminBoard bno);
}
