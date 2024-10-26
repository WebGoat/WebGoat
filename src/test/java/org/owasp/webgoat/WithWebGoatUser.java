package org.owasp.webgoat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@WithSecurityContext(factory = WithMockWebGoatUserSecurityContextFactory.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithWebGoatUser {

  String username() default "test";

  String password() default "password";
}

class WithMockWebGoatUserSecurityContextFactory
    implements WithSecurityContextFactory<WithWebGoatUser> {
  @Override
  public SecurityContext createSecurityContext(WithWebGoatUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    WebGoatUser principal = new WebGoatUser(customUser.username(), customUser.password());
    Authentication auth =
        UsernamePasswordAuthenticationToken.authenticated(
            principal, "password", principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}
