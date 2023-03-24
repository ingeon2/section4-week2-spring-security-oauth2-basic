package com.codestates.oauth2_jwt.auth.handler;

import com.codestates.member.entity.Member;
import com.codestates.member.service.MemberService;
import com.codestates.oauth2_jwt.jwt.JwtTokenizer;
import com.codestates.oauth2_jwt.utils.CustomAuthorityUtils;
import com.codestates.stamp.Stamp;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    //SimpleUrlAuthenticationSuccessHandler를 상속하면 Redirect를 손쉽게 할 수 있는 getRedirectStrategy().sendRedirect() 같은 API를 사용
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;
    public OAuth2MemberSuccessHandler(JwtTokenizer jwtTokenizer,
                                      CustomAuthorityUtils authorityUtils,
                                      MemberService memberService) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberService = memberService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        var oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));
        //Authentication 객체로부터 얻어낸 OAuth2User 객체로부터 Resource Owner의 이메일 주소를 얻음.

        List<String> authorities = authorityUtils.createRoles(email);
        //CustomAuthorityUtils를 이용해 권한 정보를 생성

        saveMember(email);
        //Resource Owner의 이메일 주소를 DB에 저장
        redirect(request, response, email, authorities);
        //Access Token과 Refresh Token을 생성해서 Frontend 애플리케이션에 전달하기 위해 Redirect

    }

    
    
    //아래는 다 private, 여기 클래스에서 사용
    private void saveMember(String email) {
        Member member = new Member(email);
        member.setStamp(new Stamp());
        memberService.createMember(member);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String username, List<String> authorities) throws IOException {

        String accessToken = delegateAccessToken(username, authorities);
        String refreshToken = delegateRefreshToken(username);
        //JWT Access Token과 Refresh Token을 생성

        String uri = createURI(accessToken, refreshToken).toString();
        //Frontend 애플리케이션 쪽의 URL을 생성

        getRedirectStrategy().sendRedirect(request, response, uri);
        // SimpleUrlAuthenticationSuccessHandler에서 제공하는 sendRedirect() 메서드를 이용해 Frontend 애플리케이션 쪽으로 리다이렉트
    }

    private String delegateAccessToken(String username, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roles", authorities);

        String subject = username;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String delegateRefreshToken(String username) {
        String subject = username;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }


    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParms = new LinkedMultiValueMap<>();
        queryParms.add("access_token", accessToken);
        queryParms.add("refresh_token", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                //.port(80)
                .path("/receive-token.html")
                .queryParams(queryParms)
                .build()
                .toUri();
    }


}

//OAuth2 인증이 성공적으로 수행되면 호출되는 핸들러인 OAuth2MemberSuccessHandler 코드