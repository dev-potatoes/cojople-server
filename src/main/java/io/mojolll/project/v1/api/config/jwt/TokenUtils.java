package io.mojolll.project.v1.api.config.jwt;

import io.jsonwebtoken.*;
import io.mojolll.project.v1.api.user.model.User;
import io.mojolll.project.v1.api.user.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j

public final class TokenUtils {

    public static String generateJwtAccessToken(User user) {
        return setJwt(user, generateAccessTokenExpireTime(), JwtProperties.ACCESS_SECRET);
    }

    public static String generateJwtRefreshToken(User user) {
        return setJwt(user, createExpireDateForOneYear(), JwtProperties.REFRESH_SECRET);
    }

    private static String setJwt(User user, Date expireDate, String secret) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(user.getEmail())
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, createSigningKey(secret));
        return builder.compact();
    }

    public static boolean isValidAccessToken(String token) {
        return isValidToken(token,JwtProperties.ACCESS_SECRET);
    }

    public static boolean isValidRefreshToken(String token) {
        return isValidToken(token,JwtProperties.REFRESH_SECRET);
    }
    private static boolean isValidToken(String token,String secret) {
        try {
            Claims claims = getClaimsFormToken(token, secret);
            log.info("expireTime:{}",claims.getExpiration());
            log.info("email:{}",claims.get("email"));
            log.info("role:{}",claims.get("role"));
            return true;

        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        }
    }

    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    private static Date generateAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME);
    }

    private static Date createExpireDateForOneYear() {
        // 토큰 만료시간은 30일으로 설정
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 14);
        return c.getTime();
    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private static Map<String, Object> createClaims(User user) {
        // 공개 클레임에 사용자의 이름과 이메일을 설정하여 정보를 조회할 수 있다.
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", user.getEmail());
        claims.put("role", user.getRoles());

        return claims;
    }

    private static Key createSigningKey(String secret) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    //parser() ->  throws ExpiredJwtException, MalformedJwtException, SignatureException
    private static Claims getClaimsFormToken(String token, String secret)  {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .parseClaimsJws(token).getBody();
    }

    public static String getUserEmailFromAccessToken(String token) {
        Claims claims = getClaimsFormToken(token, JwtProperties.ACCESS_SECRET);
        return (String) claims.get("email");
    }

//    private static UserRole getRoleFromAccessToken(String token) {
//        Claims claims = getClaimsFormToken(token,JwtProperties. ACCESS_SECRET);
//        return (UserRole) claims.get("role");
//    }
    public static Date getExpireTimeFromAccessToken(String token){
        Claims claims = getClaimsFormToken(token, JwtProperties.ACCESS_SECRET);
        return claims.getExpiration();
    }

    public static Date getExpireTimeFromRefreshToken(String token){
        Claims claims = getClaimsFormToken(token, JwtProperties.REFRESH_SECRET);
        return claims.getExpiration();
    }

    // JWT 에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = principalDetailsService.loadUserByUsername(this.getUserEmail(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
}
