package com.connectravel.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class TokenProvider {

    // 시크릿키는 UUID를 쓰거나, 어렵게 쓰거나
    private static final String SECRET_KEY = "FlRpX30pMqDbiAkmlfArbrmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9M10icegkpi46AdvrcXlE6CmTUBc6IFbTPiD";

    public String create(UserDetails userDetails) {
        // 유효 기간 설정
        Date expiryDate = Date.from(Instant.now().plus(360, ChronoUnit.MINUTES));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // HS512 알고리즘 이용 , 시크릿키
                .setSubject(userDetails.getUsername()) // 대상을 email로 설정
                .setIssuer("room app") // 발급자 설정. 해당 JWT을 발급한 대상
                .setIssuedAt(new Date()) // 발급 시작 시점
                .setExpiration(expiryDate) // 발급 종료 시점
                .compact(); // JWT을 문자열로 반환
    }

    public String createRefreshToken(UserDetails userDetails) {
        Date expiryDate = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userDetails.getUsername())
                .setIssuer("room app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser() // JWT 검증 파서 생성
                .setSigningKey(SECRET_KEY) // 시크릿키 설정
                .parseClaimsJws(token) // 검증된 JWT을 파싱 (불러옴)
                .getBody(); // 파싱된 JWT의 BODY를 불러옴
        return claims.getSubject(); // 해독한 token값을 보냄
    }

    // JWT을 이용하면, JSON을 생성, 서명, 인코딩, 디코딩, 파싱하는 작업을 하지 않아도 됨
}
