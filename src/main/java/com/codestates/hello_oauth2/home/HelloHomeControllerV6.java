package com.codestates.hello_oauth2.home;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloHomeControllerV6 {
    @GetMapping("/hello-oauth2")
    public String home(@RegisteredOAuth2AuthorizedClient("google")OAuth2AuthorizedClient authorizedClient) {
        //위에서 @RegisteredOAuth2AuthorizedClient 애너테이션을 이용해
        //아예 OAuth2AuthorizedClientRepository에 저장되어 있는 OAuth2AuthorizedClient를
        //파라미터로 전달받아서 Access Token 정보를 얻음.

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println("Access Token Value: " + accessToken.getTokenValue());
        System.out.println("Access Token Type: " + accessToken.getTokenType().getValue());
        System.out.println("Access Token Scopes: " + accessToken.getScopes());
        System.out.println("Access Token Issued At: " + accessToken.getIssuedAt());
        System.out.println("Access Token Expires At: " + accessToken.getExpiresAt());
        //로그 남기기 위해서~

        return "hello-oauth2";
    }
}

//OAuth2AuthorizedClient를 핸들러 메서드의 파라미터로 전달받는 방법
//OAuth2AuthorizedClient를 핸들러 메서드의 파라미터로 전달받는 방법