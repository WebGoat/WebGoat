package org.owasp.webgoat.container;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.container.session.WebGoatSession;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class WebSecurityConfigTest {

    @Test
    void passwordEncoderBean_shouldBePresentAndUseBCrypt() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        WebGoatUserDetailsService userDetailsService = Mockito.mock(WebGoatUserDetailsService.class);
        WebGoatSession webGoatSession = Mockito.mock(WebGoatSession.class);

        WebSecurityConfig config = new WebSecurityConfig(userDetailsService, webGoatSession);
        context.registerBean(WebSecurityConfig.class, () -> config);
        context.refresh();

        try {
            PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

            assertNotNull(encoder);
            assertTrue(
                    encoder instanceof BCryptPasswordEncoder);
        } finally {
            context.close();
        }
    }
}
