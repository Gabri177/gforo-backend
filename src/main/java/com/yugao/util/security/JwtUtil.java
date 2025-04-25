package com.yugao.util.security;

import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.accessTokenExpiredMillis}")
    private long accessTokenExpiredMillis;

    @Value("${jwt.refreshTokenExpiredMillis}")
    private long refreshTokenExpiredMillis;

    @Value("${jwt.secretKey}")
    private String secretKey;
    private SecretKey key;

    @PostConstruct
    public void init() {
        try {
            if (secretKey == null || secretKey.length() < 32) {
                throw new BusinessException(ResultCode.JWT_SETTING_ERROR);
            }
            key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResultCode.JWT_INIT_ERROR);
        }
    }

    /**
     * 用于生成token
     * @param userId
     * @param expiredMillis
     * @return token
     */
    public String generateToken(String userId, long expiredMillis) {

        return Jwts.builder()
                // 注意 这里保存的是user表的用户id 是primary key
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiredMillis))
                // 可以添加自定义信息 看后面需要吧
                // .claim("username", username)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 用于生成access token 过期时间为1小时
     * @param userId
     * @return token
     */
    public String generateAccessToken(String userId) {
        // 1 hour
        return generateToken(userId, accessTokenExpiredMillis);
    }

    /**
     * 用于生成refresh token 过期时间为7天
     * @param userId
     * @return token
     */
    public String generateRefreshToken(String userId) {
        // 7 days
        return generateToken(userId, refreshTokenExpiredMillis);
    }

    /**
     * 用于解析token
     * @param token
     * @return username
     */
    public String getUserIdWithToken(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();  // 直接从 ExpiredJwtException 获取 userId
        } catch (Exception e) {
            System.out.println("Error extracting userId from expired token: " + e.getMessage());
            return null;
        }
    }

    /**
     * 用于验证token
     * @param token
     * @return boolean
     */
    public boolean validateToken(String token){
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
