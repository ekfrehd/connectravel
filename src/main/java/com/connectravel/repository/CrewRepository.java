package com.connectravel.repository;

import com.connectravel.constant.SportEnum;
import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Participation;
import com.connectravel.domain.entity.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    //지역 검색 By String
    Page<Crew> findByStrictContaining(Pageable pageable, String strict);

//    Page<Crew> findByDeletedAtIsNullAndStrictContaining(Pageable pageable, String strict);

    //운동 검색 By Enum
    @Query("select s from Crew s where s.sportEnum=:sport or s.sportEnum=:sport2 or s.sportEnum=:sport3")
    Page<Crew> findBySportEnum(Pageable pageable, @Param("sport") SportEnum sport, @Param("sport2") SportEnum sport2, @Param("sport3") SportEnum sport3);

    Page<Crew> findByStrictContainsAndSportEnumIn(String strict, List<SportEnum> sportEnum, Pageable pageable);

    Long countBy();

    Crew findByUserAndAndId(Member user,Long id);

    Long countByStrictContaining(@Param("strictValue") String strictValue);

    @Query("select count(s.id) from Crew s where s.sportEnum=:sport")
    Long countBySportEnum(@Param("sport") SportEnum sport);



//    List<Crew> findByDeletedAtAndStrictContaining(@Nullable LocalDateTime deletedAt, String strict, Pageable pageable);

    // 참여 중인 모임
    Page<Crew> findByAndParticipationsIn(List<Participation> participation, Pageable pageable);
    long countByAndParticipationsIn(List<Participation> participation);



    Optional<Crew> findByChatRoom(ChatRoom room);

    Crew findByUser(Member user);
    List<Crew> findCrewsByUserUsername(String username);

    Page<Crew> findByDeletedAtNull(Pageable pageable);
}
