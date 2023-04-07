package io.mojolll.project.v1.api.oauth2.service;

import io.mojolll.project.v1.api.oauth2.model.ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 어떤 서비스인지 반환값을 우리가 만든 모델로 만들어줘야 된다. 선택된 모델에 의해서 값이 최종적으로 db에 저장되야되야 된다.
        ProviderUser providerUser = super.providerUser(clientRegistration, oAuth2User);

        //회원가입
        super.register(providerUser,userRequest);

        return oAuth2User;
    }
}
