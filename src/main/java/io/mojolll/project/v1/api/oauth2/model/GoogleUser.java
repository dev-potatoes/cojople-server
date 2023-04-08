package io.mojolll.project.v1.api.oauth2.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;


public class GoogleUser extends OAuth2ProviderUser{

    //CommonOAuth2Provider class google은 openid
    //OAuth2AuthorizationRequestRedirectFilter보기 code 받는거
    //AbstractAuthenticationProcessingFilter랑 OAuth2LoginAuthenticationFilter가 clamis정보
    //사용자 정보를 가지고와서 인증을 받은 최종 객체 Oauth2User객체
    public GoogleUser(OAuth2User oAuth2User, ClientRegistration clientRegistration){
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);
    }

    //제공자 별로 차이가 나는 것들 Override
    @Override
    public String getId() {
        //사용자 정보가 Clamis 형태로 Attributes에 담겨져 있다. google은 sub
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("sub");
    }
}
