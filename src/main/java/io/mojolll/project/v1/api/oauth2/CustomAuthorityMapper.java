package io.mojolll.project.v1.api.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static io.mojolll.project.v1.api.oauth2.RoleConstant.*;

// SimpleAuthorityMapper 내용
public class CustomAuthorityMapper implements GrantedAuthoritiesMapper {

    @Override
    public Set<GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            mapped.add(mapAuthority(authority.getAuthority()));
        }
        return mapped;
    }

    //여기서 필터링해서 Add google 같은경우 http://google.com/dddd/ddd.email 이런식으로 들어옴
    private GrantedAuthority mapAuthority(String name) {
        if(name.lastIndexOf(".") > 0){
            int index = name.lastIndexOf(".");
            name = SCOPE + name.substring(index+1);
        }
        if (!name.startsWith(ROLE)) {
            name = ROLE + name;
        }
        return new SimpleGrantedAuthority(name);
    }
}
