package io.mojolll.project.v1.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User){

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if (oAuth2AuthenticationToken != null){
            //네이버 고려해야 된다.
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String name = (String) attributes.get("name"); //naver이면 다시 Map 객체로 나온다,


            if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("naver")){
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                name = (String) response.get("name");
            }
            log.info("name{}:",name);
        }

        return "index";
    }

    @GetMapping("/api/user")
    //oidc가 아니라
    public String user(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User){
        log.info("authentication{}:",authentication);
        log.info("oAuth2User{}:",oAuth2User);
        return "home";
    }
    @GetMapping("/api/oidc")
    //oidc가 아니라
    public String oidc(Authentication authentication, @AuthenticationPrincipal OidcUser oidcUser){
        log.info("authentication{}:",authentication);
        log.info("oidcUser{}:",oidcUser);
        return "home";
    }
}
