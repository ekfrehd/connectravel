package org.ezone.room.security;

import lombok.RequiredArgsConstructor;
import org.ezone.room.service.CustomUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider; // 토큰 인쇄기 장착!

    @Autowired
    CustomUserDetailsService customUserDetailsService; // 커스텀한 스프링 시큐리티 로그인 서비스를 장착!

    private String parseBearerToken(HttpServletRequest request){ // 해독기계라고 생각하면 됨
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        // StringUtils : String에 대한 null, split, index 가능한 클래스
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7).trim();
        }
        return null;

        // 토큰의 형식
        // HEADER.PAYLOAD.VERIFY SIGNATURE
        // PAYLOAD에 로그인한 Email이 찍혀 있음. 궁금하면 밑에꺼 복사해서 확인해보셈 https://jwt.io
        // eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJibGJsOTg1N0BkYXVtLm5ldCIsImlzcyI6InJvb20gYXBwIiwiaWF0IjoxNjgwNzU5Mzg3LCJleHAiOjE2ODMzNTEzODd9.dNP4UERy0Iwbz7f-kFY01ruZ-Uqz6NjFglull89XNJuDofA5FQa38ZL-wTqXtY3GWkz-LLftRI5kzEsdJvcXxg
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) {
        try{
            // 요청에서 토큰 가져오기
            String token = parseBearerToken(request); // 해독기계로 해독한 토큰을
            // log.info("Filter is running...."); // 해독
            if (token != null && !token.equalsIgnoreCase("null")){ // 토큰이 진짜인지 감짜인지 판별
                String userId = tokenProvider.validateAndGetUserId(token); // 토큰을 변환한 결과를 userid로 한다.
                // customUserDetailsService을 통해 userId의 유효성을 확인 후, CustomUserDetails 객체에 저장한다
                CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
                // 결과가 맞으면 권한 부여 (// userDetail을 통해 id, password, role 부여)
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext); // SecurityContextHolder에 로그인 정보 담기
            }
            filterChain.doFilter(request, response); // 필터 적용을 해서 Request -> Response 구조로 만든다.
        }catch (Exception e){
            log.info("invaild token : " + e);
        }
    }
}
