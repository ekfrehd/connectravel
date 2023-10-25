package org.ezone.room.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    // 카카오 로그인의 1단계 클래스 ( 카카오, 구글 로그인 인증 )
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // DefaultOAuth2UserService의 기존 loadUser를 호출한다. 이 메서드가 user-info-uri를 이용해 사용자 정보를 가져오는 부분이다.
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        String authProvider = userRequest.getClientRegistration().getClientName();

        if ("google".equals(authProvider)) {
            String email = (String) oAuth2User.getAttributes().get("email");
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", email);
            userInfo.putAll(oAuth2User.getAttributes());

            return new ApplicationOAuth2User(email, userInfo);

        } else if ("kakao".equals(authProvider)) {
            // JSON으로 들어오니까 "email"의 벨류 값을 빼는것.
            String username = (String) ((Map<String, Object>) oAuth2User.getAttributes().get("kakao_account")).get("email");

            Map<String, Object> userInfo = new HashMap<>(); // 정보를 MAP형식으로
            userInfo.put("email", username); // 사용자의 이메일 주소 정보 추가
            userInfo.putAll(oAuth2User.getAttributes()); // 기존 사용자 정보 추가

            return new ApplicationOAuth2User(username, userInfo); // 주의 : 소셜 로그인 자체는 "성공"이 된 걸로 처리를 하고 객체를 들고가는거임.
            // 어쨌든 소셜 로그인의 정보를 리턴함.

        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("unsupported_provider"));
        }
    }
}