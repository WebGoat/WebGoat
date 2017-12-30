package org.owasp.webgoat.plugin.introduction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.owasp.webgoat.session.WebgoatContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 5/21/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson5aTest extends LessonTest {

    @Autowired
    private WebgoatContext context;

    @Before
    public void setup() throws Exception {
        SqlInjection sql = new SqlInjection();
        when(webSession.getCurrentLesson()).thenReturn(sql);
        when(webSession.getWebgoatContext()).thenReturn(context);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void knownAccountShouldDisplayData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5a")
                .param("account", "Smith"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.output", containsString("<p>USERID, FIRST_NAME")));
    }

    @Test
    public void unknownAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5a")
                .param("account", "Smithh"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(messages.getMessage("NoResultsMatched"))))
                .andExpect(jsonPath("$.output").doesNotExist());
    }

    @Test
    public void sqlInjection() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5a")
                .param("account", "smith' OR '1' = '1"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", containsString("You have succeed")))
                .andExpect(jsonPath("$.output").doesNotExist());
    }

    @Test
    public void sqlInjectionWrongShouldDisplayError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5a")
                .param("account", "smith' OR '1' = '1'"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", containsString(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.output", is("malformed string: '1''")));
    }
}