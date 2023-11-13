package com.connectravel.repository;

import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Participation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation,Long> {
    Boolean existsByCrewAndAndUser(Crew crew, Member user);
    List<Participation> findByUser(Member user);
    Optional<Participation> findByCrewAndUser(Crew crew,Member user);
    List<Participation> findByCrew(Crew crew);
    List<Participation> findByStatusAndUser(Integer status, Member user);
}
