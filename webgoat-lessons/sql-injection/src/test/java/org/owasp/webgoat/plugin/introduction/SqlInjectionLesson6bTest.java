package org.owasp.webgoat.plugin.introduction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**a
 * @author nbaars
 * @since 6/16/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson6bTest extends LessonTest {

    @Before
    public void setup() throws Exception {
        when(webSession.getCurrentLesson()).thenReturn(new SqlInjection());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void submitCorrectPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack6b")
                .param("userid_6b", "passW0rD"))

                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void submitWrongPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack6b")
                .param("userid_6b", "John"))

                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }

}