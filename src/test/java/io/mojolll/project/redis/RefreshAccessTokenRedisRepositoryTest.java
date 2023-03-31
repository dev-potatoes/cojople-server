package io.mojolll.project.redis;

import io.mojolll.project.v1.api.redis.refresh.RefreshTokenFromRedis;
import io.mojolll.project.v1.api.redis.refresh.RefreshTokenRedisRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RefreshAccessTokenRedisRepositoryTest {

    @Autowired
    RefreshTokenRedisRepository refreshTokenRedisRepository;

    @BeforeEach
    public void clear(){
        refreshTokenRedisRepository.deleteAll();
    }

    @DisplayName("save")
    @Test
    public void save() throws Exception{
        //given
        String refreshToken = "refreshToken";
        String email = "email";
        Long expiration = 3000L;
        RefreshTokenFromRedis refreshTokenFromRedis = RefreshTokenFromRedis.createRefreshToken(refreshToken, email, expiration);

        //when
        refreshTokenRedisRepository.save(refreshTokenFromRedis);

        //then
        RefreshTokenFromRedis find = refreshTokenRedisRepository.findById(refreshToken).get();


        assertAll(
                () -> assertEquals(refreshToken,find.getId()),
                () -> assertEquals(email,find.getEmail()),
                () -> assertEquals(expiration/1000,find.getExpiration())
        );
    }
}