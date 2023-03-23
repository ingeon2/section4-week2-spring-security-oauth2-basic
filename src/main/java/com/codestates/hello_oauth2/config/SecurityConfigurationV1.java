package com.codestates.hello_oauth2.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
public class SecurityConfigurationV1 {
    //@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests(authorize -> authorize
                        //인증된 request에 대해서만 접근을 허용하도록 authorize.anyRequest().authenticated()를 추가
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults()); // OAuth 2 로그인 인증을 활성화

        return http.build();
    }
}
