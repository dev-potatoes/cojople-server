package io.mojolll.project.v1.api.oauth2.service;

import io.mojolll.project.v1.api.oauth2.model.*;
import io.mojolll.project.v1.api.oauth2.repository.SocialUserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Getter
@Slf4j
public abstract class AbstractOAuth2UserService {

    @Autowired
    private SocialUserRepository socialUserRepository;
    @Autowired
    private SocialUserService socialUserService;


    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {

        Optional<SocialUser> user = socialUserRepository.findByUsername(providerUser.getUsername());
        if (user.isEmpty()){
            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            socialUserService.register(registrationId,providerUser);
        }
        else log.info("socialUser{}:",user.get());

    }

    public ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        //구분 기준은 registration_id 로 구분할 수 있다.
        String registrationId = clientRegistration.getRegistrationId();

        switch (registrationId) {
            case "keycloak":
                return new KeycloakUser(oAuth2User,clientRegistration);
            case "google":
                return new GoogleUser(oAuth2User,clientRegistration);
            case "naver":
                return new NaverUser(oAuth2User,clientRegistration);
            case "kakao":
                return new KakaoUser(oAuth2User,clientRegistration);
        }
        return null;
    }
}
