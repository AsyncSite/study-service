package com.asyncsite.studyservice.common.adapter.in.web.filter;

import com.asyncsite.studyservice.common.domain.security.GatewayAuthenticationDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_NAME_HEADER = "X-User-Name";
    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_ROLES_HEADER = "X-User-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String userId = request.getHeader(USER_ID_HEADER);
        String userName = request.getHeader(USER_NAME_HEADER);
        String userEmail = request.getHeader(USER_EMAIL_HEADER);
        String userRoles = request.getHeader(USER_ROLES_HEADER);
        
        log.debug("Gateway headers - Path: {}, UserId: {}, Email: {}, Roles: {}", 
            request.getRequestURI(), userId, userEmail, userRoles);

        if (StringUtils.hasText(userId) && StringUtils.hasText(userEmail)) {
            log.debug("Authenticating user from gateway headers: userId={}, email={}", userId, userEmail);
            
            List<SimpleGrantedAuthority> authorities = parseAuthorities(userRoles);
            
            UserDetails userDetails = User.builder()
                .username(userEmail)
                .password("")
                .authorities(authorities)
                .build();
            
            GatewayAuthenticationDetails authDetails = GatewayAuthenticationDetails.builder()
                .userId(userId)
                .userName(userName)
                .userEmail(userEmail)
                .roles(userRoles)
                .build();
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
            authToken.setDetails(authDetails);
            
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.debug("Successfully authenticated user: {}", userEmail);
        } else {
            log.debug("No gateway authentication headers found in request to: {}", request.getRequestURI());
        }
        
        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> parseAuthorities(String roles) {
        if (!StringUtils.hasText(roles)) {
            return Collections.emptyList();
        }
        
        return Arrays.stream(roles.split(","))
            .map(String::trim)
            .filter(StringUtils::hasText)
            .map(role -> {
                if (!role.startsWith("ROLE_")) {
                    return "ROLE_" + role;
                }
                return role;
            })
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}