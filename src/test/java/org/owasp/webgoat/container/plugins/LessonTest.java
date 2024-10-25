package org.owasp.webgoat.container.plugins;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.owasp.webgoat.WithWebGoatUser;
import org.owasp.webgoat.container.WebGoat;
import org.owasp.webgoat.container.i18n.Language;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author nbaars
 * @since 5/20/17.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WebGoat.class)
@TestPropertySource(
    locations = {
      "classpath:/application-webgoat.properties",
      "classpath:/application-webgoat-test.properties"
    })
@WithWebGoatUser
public abstract class LessonTest {

  @LocalServerPort protected int localPort;
  protected MockMvc mockMvc;
  @Autowired protected WebApplicationContext wac;
  @Autowired protected PluginMessages messages;
  @Autowired private Function<String, Flyway> flywayLessons;
  @Autowired private List<Initializable> lessonInitializers;
  @MockBean private Language language;

  @MockBean private ClientRegistrationRepository clientRegistrationRepository;

  @Value("${webgoat.user.directory}")
  protected String webGoatHomeDirectory;

  @BeforeEach
  void init() {
    when(language.getLocale()).thenReturn(Locale.getDefault());
    WebGoatUser user =
        (WebGoatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    flywayLessons.apply(user.getUsername()).migrate();
    lessonInitializers.forEach(init -> init.initialize(user));
  }
}
