package com.webapp.InvoiceManagementApp.Security;

import com.webapp.InvoiceManagementApp.Service.CustomerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final CustomerService customerService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer")) {
      filterChain.doFilter(request, response);
      return;
    }

    var token = authHeader.split(" ")[1];
    if (!jwtTokenUtil.validate(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    var email = jwtTokenUtil.getUsername(token);
    var user = customerService.loadUserByUsername(email);

    if (user == null) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    var authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}
