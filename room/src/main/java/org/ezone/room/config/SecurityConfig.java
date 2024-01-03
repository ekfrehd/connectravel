package org.ezone.room.config;

import org.ezone.room.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 스프링 시큐리티 로그인 핵심 설정
    // 그러므로 각종 로그인 기능을 장착 해줘야함
    @Autowired
    CustomUserDetailsService customUserDetailsService;

//    @Autowired
//    CustomOauth2UserService customOauth2UserService;
//
//    @Autowired
//    TokenProvider tokenProvider;
//
//    @Autowired
//    JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Autowired
//    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests(
//        )
//                        .anyRequest()
//                                .authenticated();

        http.formLogin() // 밑엔 참고하라고 주석 달아 둔거.
                .loginPage("/member/logintest")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .passwordParameter("password")
                .and()
                .logout() // 로그아웃은 스프링 시큐리티에게 전담하게 하는게 속편함.
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/");
//                .and()
//                .oauth2Login().redirectionEndpoint()
//                .baseUri("/member/login/oauth2/code/**"); // 소셜 로그인 페이지 담당하는 설정

        http.sessionManagement().maximumSessions(2).maxSessionsPreventsLogin(false);

//        http.oauth2Login() // 소셜 로그인 설정
//                .userInfoEndpoint()
//                .userService(customOauth2UserService) // 로그인 정보 1단계
//                .and()
//                .successHandler(oAuth2AuthenticationSuccessHandler()); // 2단계를 순서대로 세팅 하는거
//
//        http.authorizeRequests() // 경로 설정 // antMatchers : 일반적인 경로 설정 // mvc : mvc 패턴의 경로 설정 (ex : /board/{id})
//                .antMatchers("/admin","/admin/**").hasRole("ADMIN") // 관리자만 허용
//                .antMatchers("/seller/**").access("hasAnyRole('ADMIN', 'SELLER')") // 관리자 또는 판매자만
//                .antMatchers("/reservation/**").authenticated() //인증자만 허용
//                .antMatchers("/product/**", "/","/page/**", "/review/**", "/**/**/**",
//                        "/member/**", "/member/login/oauth2/code/**").permitAll() // 모두 허용
//                .mvcMatchers("/css/**", "/js/**", "/img/**", "/imgtest/**").permitAll()
//                .mvcMatchers("/images/**", "/product/**").permitAll()
//                .mvcMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated();
//
//        http.exceptionHandling() // 예외 발생시 어디로 리다이렉트 시키겠다
//                .authenticationEntryPoint(customAuthenticationEntryPoint);
//
//        // 스프링 시큐리티 필터가 돌기전에 해야하는 것들
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        // csrf 공격 막는 기능 끄기
//        http.csrf().disable();
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//    }
//
//    // 비밀번호 암호화 세팅
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean(); // 로그인 기능 외부로 쓰기위해서
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
//        return new OAuth2AuthenticationSuccessHandler();
//    }

}