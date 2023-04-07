package io.mojolll.project.v1.api.oauth2.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialUser {

    private String registrationId;
    @Id
    private String id;
    private String username;
    private String password;
    private String provider;
    private String email;


    //User랑 같은 role로 묶어서 처리하기
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "social_user_authorities", joinColumns = @JoinColumn(name = "social_user_id"))
    @Column(name = "authority")
    private List<String> authorities;

    public List<? extends GrantedAuthority> getAuthorities() {
        return this.getAuthorities().stream()
                .map(authorities -> new SimpleGrantedAuthority(authorities.getAuthority()))
                .collect(Collectors.toList());
    }
}
