package org.ezone.room.repository;

import org.ezone.room.entity.AccommodationEntity;
import org.ezone.room.entity.AccommodationImgEntity;
import org.ezone.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccommodationImgRepository extends JpaRepository<AccommodationImgEntity, Long> {

    @Query("SELECT i from AccommodationImgEntity i where i.accommodationEntity = :ano")
    List<AccommodationImgEntity> GetImgbyAccommodationId(@Param("ano") AccommodationEntity ano);

}
