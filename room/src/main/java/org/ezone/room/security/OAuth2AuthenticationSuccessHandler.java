package org.ezone.room.security;

import lombok.extern.log4j.Log4j2;
import org.ezone.room.entity.Member;
import org.ezone.room.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 소셜 로그인 2단계

    @Autowired
    MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws  IOException, ServletException {

        // 소셜 로그인에 저장된 정보를 불러옴
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String email = authToken.getPrincipal().getAttribute("email"); // DB에 있는지 email로 조회해야하니까 email을 불러온다

        if (!memberRepository.existsByEmail(email)) { // DB에 있나염?
            // 없으면 회원 가입 페이지로 리다이렉트 - session으로 데이터 줄려다가 안 돼서 빡쳐서 email을 파라미터로 보내기로 함
            getRedirectStrategy().sendRedirect(request, response, "/member/join?error=signup&email=" + email);
            // 소셜로그인 자체로는 스프링 시큐리티 로그인에 성공한 걸로 인식하기 때문에 로그아웃 처리를 해줘야됨.
            SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
            securityContextLogoutHandler.setInvalidateHttpSession(true); // 세션 무효화 처리
            securityContextLogoutHandler.logout(request, response, authentication); // 로그아웃 처리
            return;
        }
        // try catch 해야하는데 생각이 안남 걍 다시 불러오기
        Member member = memberRepository.findByEmail(email);
        CustomUserDetails customUserDetails = new CustomUserDetails(member); // Customer한 UserDetails에 로그인된 정보 집어넣기
        TokenProvider tokenProvider = new TokenProvider(); // 토큰 인쇄기 불러오기
        String token = tokenProvider.create(customUserDetails); // 토큰 발행!
        SecurityContextHolder.getContext().setAuthentication // 스프링 시큐리티에도 로그인 사실 알려주기. 여러번 쓰지만 로그인한 정보를 담기위해 CustomUserDetails애 담아야함!
                (new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()));
                // "로그인" 클래스 불러오기 ( 로그인한유저정보 (member), Crendentials : password(소셜 로그인이라 비밀번호가 없어서 null), 권한)

        response.getWriter().write(token); // 토큰 발행 사실을 알려줌

        super.onAuthenticationSuccess(request, response, authentication); // 로그인 성공됐다고 알리기!
    }
}