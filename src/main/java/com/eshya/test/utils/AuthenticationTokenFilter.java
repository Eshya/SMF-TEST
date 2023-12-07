package com.eshya.test.utils;

import com.eshya.test.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


public class AuthenticationTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateToken(jwt)) {
    	  
        String username = jwtUtils.getUsernameFromToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Iterasi melalui koleksi peran
        for (GrantedAuthority authority : authorities) {
          String role = authority.getAuthority();
          logger.info("Username: "+ username +",Role: " + role);
        }

        logger.info("Username: "+ username +",Role: " + authorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, SecurityContextHolder.getContext().getAuthentication(),
            userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e.getMessage());
    }

    filterChain.doFilter(request, response);
  }


  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth)) {
      return headerAuth.substring(0, headerAuth.length());
    }

    return null;
  }
}

