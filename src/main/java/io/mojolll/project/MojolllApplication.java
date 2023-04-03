package io.mojolll.project;

import io.mojolll.project.v1.api.redis.logout.LogoutAccessTokenRedisRepository;
import io.mojolll.project.v1.api.redis.refresh.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MojolllApplication {
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(MojolllApplication.class, args);
	}

}
