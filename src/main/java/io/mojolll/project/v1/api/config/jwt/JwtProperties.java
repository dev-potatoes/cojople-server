package io.mojolll.project.v1.api.config.jwt;

public interface JwtProperties {

    String ACCESS_SECRET = "kem4328";
    String REFRESH_SECRET = "cojopleProjectWithMinsub";
    int EXPIRATION_TIME = 10;
            //3600000;
    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_HEADER_STRING = "Authorization";
}
