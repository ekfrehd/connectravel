package com.connectravel.security.service;

import com.connectravel.domain.entity.Member;
import com.connectravel.repository.MemberRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userDetailsService")
@Getter @Setter
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpServletRequest request;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        Member member = memberOptional.orElseThrow(() -> {
            if (memberRepository.countByUsername(email) == 0) {
                return new UsernameNotFoundException("No user found with username: " + email);
            }
            return new UsernameNotFoundException("User found by username, but member is null");
        });

        Set<String> userRoles = member.getMemberRoles()
                .stream()
                .map(userRole -> userRole.getRoleName())
                .collect(Collectors.toSet());

        List<GrantedAuthority> collect = userRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new MemberContext(member, collect);
    }


}