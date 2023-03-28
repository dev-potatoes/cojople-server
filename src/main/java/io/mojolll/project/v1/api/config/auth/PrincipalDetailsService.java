package io.mojolll.project.v1.api.config.auth;

import io.mojolll.project.v1.api.model.User;
import io.mojolll.project.v1.api.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

//http://localhost:8080/login 올때 동작함
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PrincipalDetailsService : 진입");
        User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
        log.info("userbyUserDetails{}:",user);
//         session.setAttribute("loginUser", user);
        return new PrincipalDetails(user);
    }
}