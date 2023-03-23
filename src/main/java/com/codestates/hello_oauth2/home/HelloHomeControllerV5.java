package com.codestates.hello_oauth2.home;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class HelloHomeControllerV5 {
    //DI
    //OAuth2AuthorizedClientService는 권한을 부여받은 Client(이하 OAuth2AuthorizedClient)를 관리하는 역할을 하는데
    //OAuth2AuthorizedClientService를 이용해서 OAuth2AuthorizedClient 가 보유하고 있는 Access Token에 접근
    private final OAuth2AuthorizedClientService authorizedClientService;
    public HelloHomeControllerV5(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }



    //@GetMapping("/hello-oauth2")
    public String home(Authentication authentication) {
        var authorizedClient = authorizedClientService.loadAuthorizedClient("google", authentication.getName());
        //OAuth2AuthorizedClientService의 loadAuthorizedClient("google", authentication.getName())를 이용해
        //OAuth2AuthorizedClient 객체를 로드

        // authorizedClient.getAccessToken()를 이용해 OAuth2AccessToken 객체를 얻음.
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println("Access Token Value: " + accessToken.getTokenValue());  // Access Token의 문자열을 출력
        System.out.println("Access Token Type: " + accessToken.getTokenType().getValue());  // Token의 타입을 출력
        System.out.println("Access Token Scopes: " + accessToken.getScopes());       // 토큰으로 접근할 수 있는 리소스의 범위 목록을 출력
        System.out.println("Access Token Issued At: " + accessToken.getIssuedAt());    // 토큰의 발행일시를 출력
        System.out.println("Access Token Expires At: " + accessToken.getExpiresAt());  // 토큰의 만료일시를 출력

        return "hello-oauth2";
    }
}

//OAuth 2 인증 후, Resource Server에 접근할 때 사용되는 Access Token을 얻는 방법
//OAuth 2 인증 후, Resource Server에 접근할 때 사용되는 Access Token을 얻는 방법
//OAuth 2 인증 후, Resource Server에 접근할 때 사용되는 Access Token을 얻는 방법