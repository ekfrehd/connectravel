package com.connectravel.repository;

import com.connectravel.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);

  Boolean existsByEmail (String email);

  int countByUsername(String username);


}