package org.ezone.room.repository;

import org.ezone.room.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository <Member,String>{

    Member findByEmail(String email);
    Boolean existsByEmail (String email);
}
