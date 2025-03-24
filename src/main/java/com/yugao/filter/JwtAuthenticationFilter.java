package com.yugao.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.constants.SecurityWhiteListConstants;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.service.RedisService;
import com.yugao.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// 因为有过滤器 先经过过滤器然后再被Security拦截
// 两个同时起作用5
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisService redisService;
//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (Arrays.stream(SecurityWhiteListConstants.URLS).anyMatch(pattern -> antPathMatcher.match(pattern, path))) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            String userId = jwtUtil.getUserIdWithToken(token);
            if (userId != null) {
                // 查询 Redis 中的 access token
                //String redisToken = redisTemplate.opsForValue().get("access_token:" + userId);
                String redisToken = redisService.get(RedisKeyConstants.userIdAccessToken(Long.parseLong(userId)));
                if (redisToken != null && redisToken.equals(token)) {
                    // 将用户信息存入 SecurityContext 否则 Security 会认为用户未登录 并栏截请求
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
                sendJsonErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), ResultCode.ACCESSTOKEN_EXPIRED, "Access token expired");
                return ;
            }
            sendJsonErrorResponse(response, HttpStatus.FORBIDDEN.value(), ResultCode.ACCESSTOKEN_UNAUTHORIZED,"Unauthorized");
            return ;
        }
        filterChain.doFilter(request, response);
    }


    /**
     * 统一返回 JSON 格式的错误信息
     */
    private void sendJsonErrorResponse(HttpServletResponse response, int code, int errorCode, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code);
        ResultFormat errorResponse = ResultFormat.error(errorCode, message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }

    private void sendJsonSuccessResponse(HttpServletResponse response, Object data, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        ResultFormat successResponse = ResultFormat.success(data, message);
        response.getWriter().write(objectMapper.writeValueAsString(successResponse));
        response.getWriter().flush();
    }
}
