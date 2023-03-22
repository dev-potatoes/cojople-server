package io.mojolll.project.v1.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    public static String createToken(String name, String key, long expireTimeMs){
        Claims claims = Jwts.claims(); // 일종의 map
        claims.put("username",name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) //만든날짜
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) //끝나는 날짜
                .signWith(SignatureAlgorithm.HS256, key) //암호화
                .compact();
    }
}
