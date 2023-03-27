package io.mojolll.project.v1.api.configuration;

import io.mojolll.project.v1.api.entity.UserRole;
import io.mojolll.project.v1.api.filter.JwtFilter;
import io.mojolll.project.v1.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // gradle에 security를 등록하면 모든 API에 인증이 필요하다고 Spring security가 default로 설정한다.
@RequiredArgsConstructor
public class AuthenticationConfig {



    private final MemberService memberService;

    @Value("${jwt.secret}")
    private String secretKey;

    // SecurityConfig, EncoderConfig는 꼭 다른 클래스 선언 순환참조 문제가 발생할 수 있다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // 인증을 UI로 할 것이아니고 token인증으로 할것이기 때문에 disable
                .csrf().disable() //csrf
                .cors().and() //Cors 도메인 다른거 허용
                .authorizeRequests()//HttpServletRequest를 사용하는 요청에 대한 접근제한 설정
//                .antMatchers("/api/**").permitAll()
                .antMatchers("/api/v1/users/join","/api/v1/users/login").permitAll() //join, login은 언제나 허용
                .antMatchers(HttpMethod.POST, "/api/v1/**").hasRole("USER")//.authenticated() //join, login을 제외한 모든 POST요청을 인증필요로 막아둠
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt 사용하는 경우 사용
                .and()
                //token을 풀기위해서는 secretKey가 필요해서 매개편수에 추가
                //JwtFilter계층을 UsernamePasswordAuthenticationFilter 앞에 두어야된다.
                //email하고 password가지고 로그인할 때 인증을 했기 때문에 서버에서 토큰을 준건데 token으로 인증을 할 것이기 때문에 추가
                .addFilterBefore(new JwtFilter(memberService,secretKey), UsernamePasswordAuthenticationFilter.class) //
                .build();
    }
}
