package com.yeonseong.board.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest
      , HttpServletResponse httpServletResponse
      , FilterChain filterChain
  ) throws ServletException, IOException {

    String token = resolveToken(httpServletRequest);

    if (token != null && jwtProvider.validateToken(token)) {
      Long memberId = jwtProvider.getMemberId(token);
      Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
