package com.connectravel.repository;

import com.connectravel.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);

  int countByUsername(String username);


    Optional<Member> findByUsername(String name);


}