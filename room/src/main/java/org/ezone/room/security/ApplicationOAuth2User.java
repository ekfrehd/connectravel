package org.ezone.room.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ApplicationOAuth2User implements OAuth2User {

    // OAuth2로 로그인한 사람 정보 담는거
    private String email; // email
    private Collection<? extends GrantedAuthority> authorities; // 로그인 사람의 권한 부여
    private Map<String, Object> attributes;
    private boolean isNewUser;

    // 스프링 시큐리티 정책 상 (개발자가 그렇게 만들었다는데 어쩌겠움) 모든 경로의 입장에서 권한이 반드시 필요하다.
    public ApplicationOAuth2User(String email, Map<String, Object> attributes) {
        this.email = email;
        this.attributes = attributes;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // 따라서, 임시 권한 증정
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.email;
    }
}