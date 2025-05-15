package com.yugao.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yugao.constants.SecurityWhiteListConstants;
import com.yugao.domain.user.User;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.security.LoginUser;
import com.yugao.service.data.UserService;
import com.yugao.service.handler.PermissionHandler;
import com.yugao.service.handler.TokenHandler;
import com.yugao.service.handler.UserHandler;
import com.yugao.util.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final TokenHandler tokenHandler;
    private final PermissionHandler permissionHandler;
    private final UserHandler userHandler;
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String token = request.getHeader("Authorization");
        String deviceId = request.getHeader("gforo-deviceId");

        System.out.println("deviceId: " + deviceId);
        if (Arrays.stream(SecurityWhiteListConstants.URLS)
                .anyMatch(pattern -> antPathMatcher.match(pattern, path)) && (
                        token == null || !token.startsWith("Bearer ")
                )) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            String userId = jwtUtil.getUserIdWithToken(token);
            if (userId != null) {
                // 查询 Redis 中的 access token
                boolean res = tokenHandler.verifyUserAccessToken(token, deviceId);
                if (res) {
                    User user = userHandler.getUser(Long.parseLong(userId));
                    Integer userLevel = permissionHandler.getUserRoleLevel(user.getId());
//                    log.info("log userLevel: {}", userLevel);
                    List<String> permissionCodes =
                                permissionHandler.getPermissionCodesByUserId(user.getId());
                    // System.out.println("permissionCodes: " + permissionCodes);
                    LoginUser loginUser = new LoginUser(
                            user.getId(),
                            user.getUsername(),
                            user.getPassword(),
                            userLevel,
                            permissionCodes
                    );
                    // BeanUtils.copyProperties(user, loginUser);
                    System.out.println("loginUser: " + loginUser);
                    // 将用户信息存入 SecurityContext 否则 Security 会认为用户未登录 并栏截请求
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
                sendJsonErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), ResultCodeEnum.ACCESSION_EXPIRED);
                return ;
            }
            sendJsonErrorResponse(response, HttpStatus.FORBIDDEN.value(), ResultCodeEnum.ACCESSION_UNAUTHORIZED);
            return ;
        }
        filterChain.doFilter(request, response);
    }


    /**
     * 统一返回 JSON 格式的错误信息
     */
    private void sendJsonErrorResponse(HttpServletResponse response, int code, ResultCodeEnum errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code);
        ResultFormat errorResponse = ResultFormat.error(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }

}
