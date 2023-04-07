package io.mojolll.project.v1.api.oauth2.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

// https://developers.naver.com/docs/login/devguide/devguide.md
public class NaverUser extends OAuth2ProviderUser{

    //사용자 정보를 가지고와서 인증을 받은 최종 객체 Oauth2User객체
    public NaverUser(OAuth2User oAuth2User, ClientRegistration clientRegistration){
        //naver는 개발가이드 보면 response/id 이런식이라서 response로 꺼낸고 id 꺼내야된다.
        super((Map<String,Object>)oAuth2User.getAttributes().get("response"), oAuth2User, clientRegistration);

    }

    //제공자 별로 차이가 나는 것들 Override
    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("email");
    }

}