package io.mojolll.project.v1.api.config.auth;

import io.mojolll.project.v1.api.model.User;
import io.mojolll.project.v1.api.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http://localhost:8080/login 올때 동작함
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : 진입");
        User user = userRepository.findByEmail(username);
        System.out.println("userbyUserDetails:" +user);
        // session.setAttribute("loginUser", user);
        return new PrincipalDetails(user);
    }
}