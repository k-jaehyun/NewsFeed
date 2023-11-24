package com.example.newsfeed_8.jwt;


import com.example.newsfeed_8.dto.CommonResponseDto;
import com.example.newsfeed_8.service.MemberDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final MemberDetailsService memberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        if(Objects.nonNull(token)){
                if(jwtUtil.validateToken(token)){
                Claims info = jwtUtil.getMemberInfoFromToken(token);

                String userId = info.getSubject();
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UserDetails memberDetails = memberDetailsService.getMemberDetails(userId);
                Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails
                        , null, memberDetails.getAuthorities());
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

            } else{
                CommonResponseDto commonResponseDto = new CommonResponseDto("토큰이 유효하지 않습니다"
                        , HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(commonResponseDto));
            }
        }

        filterChain.doFilter(request, response);
    }
}
