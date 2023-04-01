package io.mojolll.project.v1.api.redis.refresh;

import io.mojolll.project.v1.api.redis.logout.LogoutAccessTokenFromRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenFromRedis,String> {

    // @Indexed 사용한 필드만 가능
    Optional<RefreshTokenFromRedis> findByEmail(String email);
    Optional<RefreshTokenFromRedis> findByAccessToken(String accessToken);
    void deleteByEmail (String email);
}
