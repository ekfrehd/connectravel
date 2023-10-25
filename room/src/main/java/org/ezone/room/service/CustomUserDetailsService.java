package org.ezone.room.service;

import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.entity.Member;
import org.ezone.room.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Optional : 특정 조건을 조회후 반환값이 null 이면 예외처리를 하고, 존재하면 해당 값을 이용한 처리를 진행한다.
        Optional<Member> optionalUser = Optional.ofNullable(memberRepository.findByEmail(email));

        // id로 조회했는데 null이 나오면 예외 처리
        Member user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

        return new CustomUserDetails(user); // 로그인한 정보 반환
    }
}
