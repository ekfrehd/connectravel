//package com.connectravel.config;
//
//import com.connectravel.security.TokenProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    // 스프링 시큐리티 로그인 핵심 설정
//    // 그러므로 각종 로그인 기능을 장착 해줘야함
//
//    @Autowired
//    TokenProvider tokenProvider;
//
//
//
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeRequests()
//                .antMatchers("/**").permitAll() // 예시: 특정 경로에 대한 퍼미션 설정
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/member/login") // 로그인 페이지 경로
//                .defaultSuccessUrl("/view/v1/roomsall") // 로그인 성공 시 이동할 경로
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/member/logout") // 로그아웃 경로
//                .permitAll();
//
//        http.csrf().disable();
//    }
//
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
//
//
//}