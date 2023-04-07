package io.mojolll.project.v1.api.oauth2.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

public interface ProviderUser {

    String getId();
    String getUsername();
    String getPassword(); //임의로 Random으로 만들거라 의미없다.
    String getEmail();
    String getProvider(); //어디 서비스의 제공자로부터 생성되었는지 알아야 된다.

    List<? extends GrantedAuthority> getAuthorities();


    //사용자 속성을 가저오는 Attribute 서비스 제공자들로 부터 받는 정보
    Map<String, Object> getAttributes();
}
