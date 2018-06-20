package org.owasp.webgoat.plugin;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 11/17/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CSRFFeedbackTest extends LessonTest {

    @Before
    public void setup() throws Exception {
        CSRF csrf = new CSRF();
        when(webSession.getCurrentLesson()).thenReturn(csrf);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void postingJsonMessageThroughWebGoatShouldWork() throws Exception {
        mockMvc.perform(post("/csrf/feedback/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test\", \"email\": \"test1233@dfssdf.de\", \"subject\": \"service\", \"message\":\"dsaffd\"}"))
                .andExpect(status().isOk());
    }


    @Test
    public void csrfAttack() throws Exception {
        mockMvc.perform(post("/csrf/feedback/message")
                .contentType(MediaType.TEXT_PLAIN)
                .cookie(new Cookie("JSESSIONID", "test"))
                .header("host", "localhost:8080")
                .header("referer", "webgoat.org")
                .content("{\"name\": \"Test\", \"email\": \"test1233@dfssdf.de\", \"subject\": \"service\", \"message\":\"dsaffd\"}"))
                .andExpect(jsonPath("lessonCompleted", is(true)))
                .andExpect(jsonPath("feedback", StringContains.containsString("the flag is: ")));
    }
}