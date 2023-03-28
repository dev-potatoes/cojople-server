package io.mojolll.project.v1.api.config.jwt;

//git에 노출안되게 숨기기
public interface JwtProperties {

    String SECRET = "HyunMinWithMojolllProject"; // 우리 서버만 알고 있는 비밀값
    int ACCESS_TOKEN_EXPIRATION_TIME = 1800000; // 30분 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}