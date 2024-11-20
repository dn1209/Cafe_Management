package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailsService;
    private static final List<String> PUBLIC_URLS = Arrays.asList("/api/login", "/api/registerHidden", "/api/checking_register");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        String path = request.getRequestURI();
//        if ("/api/login".equals(path) || "/api/register".equals(path)) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                CustomUserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);
                if(userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                // Nếu JWT không có hoặc không hợp lệ, trả về thông báo lỗi
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid or missing JWT token\"}");
                return;
            }
        } catch (Exception ex) {
            log.error("fail on set user authentication", ex);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or missing JWT token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return PUBLIC_URLS.stream().anyMatch(path::equals);
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}