package org.owasp.webgoat.plugins;

import org.junit.Before;
import org.owasp.webgoat.i18n.Language;
import org.owasp.webgoat.i18n.Messages;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.mockito.Mockito.when;

/**
 * @author nbaars
 * @since 5/20/17.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class LessonTest {

    @LocalServerPort
    protected int localPort;
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    protected Messages messages;
    @MockBean
    protected WebSession webSession;
    @MockBean
    private Language language;

    @Before
    public void init() {
        when(language.getLocale()).thenReturn(Locale.US);
    }

}
