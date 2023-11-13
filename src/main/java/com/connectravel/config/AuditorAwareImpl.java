package com.connectravel.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    // 16행 : 현재 사용자 인증 정보를 가져온다.
    // 17행 : userId 초기화한다.
    // 18 ~ 19행 : 현재 사용자 인증 정보가 있다면 사용자 이름을 userId에 설정한다.
    // 21행 : Optional객체를 사용하여 userId 값을 반환
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if (authentication != null) {
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }
}