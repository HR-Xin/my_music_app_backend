package com.ninetrees.musicapp.utils;

import com.ninetrees.musicapp.exception.ChanException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * JWT 工具类：用于生成、校验和解析 token
 */
@Slf4j
public class JwtUtils {

    // token 有效时间：24 小时
    public static final long EXPIRE = 1000 * 60 * 60 * 24*30;
    // 加密秘钥（用于签名）
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * 生成 JWT Token
     *
     * @param id       用户ID
     * @param nickname 用户昵称
     * @return JWT字符串
     */
    public static String getJwtToken(String id, String nickname) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // 头部信息：类型
                .setHeaderParam("alg", "HS256") // 头部信息：加密算法
                .setSubject("music-user") // 自定义主题
                .setIssuedAt(now) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 过期时间
                .claim("id", id) // 载荷：用户ID
                .claim("nickname", nickname) // 载荷：昵称
                .signWith(SignatureAlgorithm.HS256, APP_SECRET) // 签名算法 + 密钥
                .compact();
    }

    /**
     * 校验 token 是否有效（通过 token 字符串）
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // 可使用日志替代
            return false;
        }
    }

    /**
     * 校验 token 是否有效（通过请求头中的 Authorization 提取 token）
     */
    public static boolean checkToken(HttpServletRequest request) {
        String jwtToken = getTokenFromHeader(request);
        if (StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // 可使用日志替代
            return false;
        }
    }

    /**
     * 从请求中提取用户 ID（根据 Authorization 头中的 Bearer token）
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        try {
            String jwtToken = getTokenFromHeader(request);
            if (StringUtils.isEmpty(jwtToken)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            String id = (String) claims.get("id");
            System.out.println("jwt获取的用户id"+id);

            return id;

        } catch (JwtException e) {
            e.printStackTrace(); // 可记录日志
            return "";
        }
    }

    /**
     * 私有方法：从请求头中提取 Bearer token
     *
     * @param request HttpServletRequest
     * @return token 字符串（去除 Bearer 前缀）
     */
    private static String getTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String substring = authHeader.substring(7);// 去除 "Bearer " 前缀

        return substring;
    }

    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(APP_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("token 已过期");
            throw new ChanException("Token 已过期"); // 自定义异常
        } catch (Exception e) {
            log.error("token 无效", e);
            return null;
        }
    }

}
