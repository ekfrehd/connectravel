package com.connectravel.repository;

import com.connectravel.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);

  Boolean existsByEmail (String email);
  Optional<Member> findByUsername(String name);
//  Optional<Member> findByUserName(String name);


  int countByUsername(String username);


}