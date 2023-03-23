package com.codestates.hello_oauth2.home;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

//@Controller
public class HelloHomeControllerV2 {
    //@GetMapping("/hello-oauth2")
    public String home() {
        var oAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //SecurityContext에서 인증된 Authentication 객체를 통해 Principal 객체를 얻음.
        System.out.println(oAuth2User.getAttributes().get("email"));
        //OAuth2User 객체에 저장되어 있는 사용자의 정보 중에서 getAttributes() 메서드를 통해 사용자의 이메일 정보를 얻음.
        return "hello-oauth2";
    }
}
//따라서 SecurityContext에서 인증된 Authentication으로 Principal 객체를 얻은 후에
//사용자의 이메일 주소를 출력했을 때, 정상적으로 이메일 주소가 출력된다면 OAuth 2 인증에 성공했다고 확신 (위의 매서드)

//인증이 정상적으로 수행되면 SecurityContext에 인증된 Authentication이 저장되는 Spring Security의 특성을 이용해
//인증된 Authentication이 사용자 정보를 잘 포함하고 있는지 확인

//인증이 정상적으로 수행되면 SecurityContext에 인증된 Authentication이 저장되는 Spring Security의 특성을 이용해
//인증된 Authentication이 사용자 정보를 잘 포함하고 있는지 확인

//인증이 정상적으로 수행되면 SecurityContext에 인증된 Authentication이 저장되는 Spring Security의 특성을 이용해
//인증된 Authentication이 사용자 정보를 잘 포함하고 있는지 확인