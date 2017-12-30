package org.owasp.webgoat.plugins;

import org.junit.Before;
import org.owasp.webgoat.i18n.Language;
import org.owasp.webgoat.i18n.PluginMessages;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.session.WebgoatContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.mockito.Mockito.when;

/**
 * @author nbaars
 * @since 5/20/17.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:/application-test.properties")
public abstract class LessonTest {

    @LocalServerPort
    protected int localPort;
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    protected PluginMessages messages;
    @MockBean
    protected WebSession webSession;
    @Autowired
    private WebgoatContext context;
    @MockBean
    private Language language;

    @Before
    public void init() {
        when(webSession.getUserName()).thenReturn("unit-test");
        when(language.getLocale()).thenReturn(Locale.getDefault());
        when(webSession.getWebgoatContext()).thenReturn(context);
    }

}
