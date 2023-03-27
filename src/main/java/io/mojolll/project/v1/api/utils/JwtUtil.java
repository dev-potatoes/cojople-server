package io.mojolll.project.v1.api.utils;

import io.jsonwebtoken.*;
import io.mojolll.project.v1.api.entity.UserRole;
import io.mojolll.project.v1.api.entity.Member;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {

    public static String getUserEmail(String token, String secretKey){

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().get("email", String.class);
    }

    public static String getRoleFromToken(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("role", String.class);
    }

    public static String createToken(Member member, String secretKey, long expireTimeMs){
        Claims claims = Jwts.claims(); // jwt claim 원하는 정보를 담아 놓음 map형태로

        claims.put("email", member.getEmail());
        claims.put("role", member.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) //만든날짜 오늘날짜
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs * 1000)) //끝나는 날짜
                .signWith(SignatureAlgorithm.HS512, secretKey) //암호화
                .compact();
    }

    public static boolean isExpired(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration() //여기까지 Date형식이다.
                .before(new Date()); //지금보다 전이면 block처리 해야된다.
    }
//    public static boolean isValidToken(String token) {
//        try {
//            Claims claims = getClaimsFormToken(token);
//            log.info("expireTime :" + claims.getExpiration());
//            log.info("email :" + claims.get("email"));
//            log.info("role :" + claims.get("role"));
//            return true;
//
//        } catch (ExpiredJwtException exception) {
//            log.error("Token Expired");
//            return false;
//        } catch (JwtException exception) {
//            log.error("Token Tampered");
//            return false;
//        } catch (NullPointerException exception) {
//            log.error("Token is null");
//            return false;
//        }
//    }
    public static String getTokenFromHeader(String authentication) {
        String token = authentication.split(" ")[1]; //0은Bearer 일테니까
        return token;
    }
}
