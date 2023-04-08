package io.mojolll.project.v1.api.oauth2.service;

import io.mojolll.project.v1.api.oauth2.RoleConstant;
import io.mojolll.project.v1.api.oauth2.model.ProviderUser;
import io.mojolll.project.v1.api.oauth2.model.SocialUser;
import io.mojolll.project.v1.api.oauth2.repository.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static io.mojolll.project.v1.api.oauth2.RoleConstant.*;

@Service
public class SocialUserService {

    @Autowired
    private SocialUserRepository socialUserRepository;

    public void register(String registrationId, ProviderUser providerUser){

        List<String> authorities = providerUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(name -> name.startsWith(SCOPE) || name.startsWith(ROLE))
                .map(name -> {
                    if(name.lastIndexOf(".") > 0){
                        int index = name.lastIndexOf(".");
                        name = SCOPE + name.substring(index+1);
                    }
                    if (!name.startsWith(ROLE)) {
                        name = ROLE + name;
                    }
                    return name;
                })
                .collect(Collectors.toList());

        SocialUser user = SocialUser.builder()
                .registrationId(registrationId)
                .id(providerUser.getId())
                .username(providerUser.getUsername())
//                .password(providerUser.getPassword())
                .provider(providerUser.getProvider())
                .email(providerUser.getEmail())
                .authorities(authorities)
                .build();

        socialUserRepository.save(user);
    }
}
