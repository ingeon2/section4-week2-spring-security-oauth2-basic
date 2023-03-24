이전 유닛까지 학습했던 로그인 인증 방식은 사용자의 크리덴셜(Credential)을 직접 관리하는 방식.  
즉, 회원 정보 중에서 로그인 인증에 사용되는 Password를 백엔드 애플리케이션 쪽에서 직접 관리하는 구조.  
하지만 OAuth 2를 통한 인증은 로그인 인증 정보를 백엔드 애플리케이션에서 직접적으로 관리하지 않음.  


핵심 포인트  
OAuth 2는 사용자 정보를 보유하고 있는 신뢰할 만한 써드 파티 애플리케이션(GitHub, Google, Facebook 등)에서  
사용자의 인증을 대신 처리해 주고 접근 권한에 대한 토큰을 발급한 뒤  
해당 토큰을 이용해 써드 파티 애플리케이션의 서비스를 사용하게 해주는 방식이다.  
  
OAuth 2를 사용하는 애플리케이션 유형  
써드 파티 애플리케이션에서 제공하는 API를 직접적으로 사용  
추가적인 인증 서비스를 제공하기 위한 용도  




핵심 포인트  
OAuth 2 인증 컴포넌트  
Resource Owner는 사용하고자 하는 Resource의 소유자를 의미한다.  
Client는 Resource Owner를 대신해 보호된 Resource에 액세스하는 애플리케이션을 의미한다.  
Resource Server는 Client의 요청을 수락하고 Resource Owner에 해당하는 Resource를 제공하는 서버를 의미한다.  
Authorization Server는 Client가 Resource Server에 접근할 수 있는 권한을 부여하는 서버를 의미한다.  
OAuth 2 인증 프로토콜의 키포인트는  
어떤 Resource를 소유하고 있는 Resource Owner를 대신하는 누군가(Client)가  
Resource Owner의 대리인 역할을 수행한다는 것이다.  
  
Authorization Grant에 따른 인증 처리 방식  
Authorization Code Grant : 권한 부여 승인 코드 방식  
Implicit Grant : 암묵적 승인 방식  
Resource Owner Password Credential Grant : 자원 소유자 자격 증명 승인 방식  
Client Credentials Grant : 클라이언트 자격 증명 승인 방식  


간단한 Spring Security 기반의 샘플 애플리케이션에 OAuth 2를 적용해 보면서 OAuth 2를 조금 더 구체적으로 이해.  


핵심 포인트  
구글의 OAuth 2 인증 시스템을 사용하기 위해서는 구글 API 콘솔에서 OAuth 클라이언트를 생성해야 한다.  
생성된 OAuth 클라이언트의 클라이언트 ID와 클라이언트 보안 비밀번호(Secret)는  
Spring Security 기반의 애플리케이션의 설정 정보로 사용되므로 유출되지 않도록 안전하게 잘 보관한다.  
  
  
hello-oauth2 안의 패키지에서 대부분 만들어냄.(config, controller)  


하나 이상의 핸들러 메서드에서 OAuth2AuthorizedClient를 사용해야 한다면  
OAuth2AuthorizedClientService를 DI 받아서 사용하는 것이 바람직.(V5)  



핵심 포인트
spring-boot-starter-oauth2-client 으로 추가한 후,  
별도의 설정을 하지 않아도 Spring Boot의 자동 구성을 통해 OAuth 2 로그인 인증 기능이 활성화된다.  
ClientRegistration은 OAuth 2 시스템을 사용하는 Client 등록 정보를 표현하는 객체이다.  
Spring Security에서 제공하는 CommonOAuth2Provider enum은  
내부적으로 Builder 패턴을 이용해 ClientRegistration 인스턴스를 제공하는 역할을 한다.(시큐리티컨피그V2)  
OAuth2AuthorizedClientService는 권한을 부여받은 Client인 OAuth2AuthorizedClient를 관리하는 역할을 한다.  
OAuth2AuthorizedClientService를 이용해서  
OAuth2AuthorizedClient 가 보유하고 있는 Access Token에 접근할 수 있다.  
OAuth2AuthorizedClientService의 loadAuthorizedClient("google", authentication.getName())를 호출하면  
OAuth2AuthorizedClientRepository를 통해 OAuth2AuthorizedClient 객체를 로드할 수 있다.  
여기까지는 그냥 OAuth2사용 알아보는 것.    
  
  
JWT와 OAut2를 결합시키기 위해 oauth2_jwt 패키지 생성.  
