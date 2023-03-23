package com.codestates.hello_oauth2.home;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class HelloHomeControllerV4 {
    //@GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User oAuth2User) {  // @AuthenticationPrincipal 애너테이션을 이용해 OAuth2User 객체를 파라미터로 직접 전달
        System.out.println("User's email in Google: " + oAuth2User.getAttributes().get("email"));

        return "hello-oauth2";
    }
}

//OAuth2User를 파라미터로 전달받는 방법