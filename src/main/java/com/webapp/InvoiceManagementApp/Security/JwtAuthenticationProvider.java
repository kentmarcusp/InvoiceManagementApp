package com.webapp.InvoiceManagementApp.Security;

import com.webapp.InvoiceManagementApp.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final BCryptPasswordEncoder passwordEncoder;
  private final CustomerService customerService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    var email = authentication.getName();
    var password = String.valueOf(authentication.getCredentials());

    var user = customerService.loadUserByUsername(email);

    if (user != null && passwordEncoder.matches(password, user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());
    }

    throw new BadCredentialsException("Bad credentials");
  }

  @Override
  public boolean supports(Class<?> authenticationType) {
    return UsernamePasswordAuthenticationToken.class.equals(authenticationType);
  }
}
