package io.mojolll.project.v1.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    public static String getUserName(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("username",String.class);
    }

    public static String createToken(String name, String secretKey, long expireTimeMs){
        Claims claims = Jwts.claims(); // jwt claim 원하는 정보를 담아 놓음 map형태로
        claims.put("username",name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) //만든날짜 오늘날짜
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) //끝나는 날짜
                .signWith(SignatureAlgorithm.HS256, secretKey) //암호화
                .compact();
    }

    public static boolean isExpired(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration() //여기까지 Date형식이다.
                .before(new Date()); //지금보다 전이면 block처리 해야된다.
    }
}
