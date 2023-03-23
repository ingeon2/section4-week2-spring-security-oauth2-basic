package com.codestates.hello_oauth2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfigurationV2 {


    //application.yml 파일에 설정되어 있는 구글의 Client ID와 Secret을 로드
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults());
        return http.build();
    }




    
    //ClientRegistrationRepository를 Bean으로 등록, ClientRegistration을 저장하기 위한 Responsitory
    //자동 구성 기능을 이용할 경우(시큐리티컨피그V1), application.yml 파일에 설정된 구글의 Client ID와 Secret 정보를 기반으로
    //우리 눈에는 보이지 않지만 내부적으로 ClientRegistrationRepository Bean이 생성되는 반면,
    //지금은 우리가 Configuration을 통해 ClientRegistrationRepository Bean을 직접 등록하고 있다는 사실을 기억.
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        var clientRegistration = clientRegistration();
        //private 메서드인 clientRegistration()을 호출해서 ClientRegistration 인스턴스를 리턴

        return new InMemoryClientRegistrationRepository(clientRegistration);
        ////ClientRegistrationRepository 인터페이스의 구현 클래스인InMemoryClientRegistrationRepository의 인스턴스를 생성
    }



    //바로 위의 Bean을 위해 ClientRegistration 인스턴스를 생성하는 private 메서드
    private ClientRegistration clientRegistration() {
        //Spring Security에서는 CommonOAuth2Provider라는 enum을 제공하는데
        //CommonOAuth2Provider 는 내부적으로 Builder 패턴을 이용해 ClientRegistration 인스턴스를 제공하는 역할
        return CommonOAuth2Provider
                .GOOGLE
                .getBuilder("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
