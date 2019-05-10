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
 * @author Benedikt Stuhrmann
 * @since 11/07/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson8Test extends LessonTest {

    @Autowired
    private WebgoatContext context;

    @Before
    public void setup() {
        SqlInjection sql = new SqlInjection();
        when(webSession.getCurrentLesson()).thenReturn(sql);
        when(webSession.getWebgoatContext()).thenReturn(context);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void oneAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack8")
                .param("name", "Smith")
                .param("auth_tan", "3SL99A"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(modifySpan(messages.getMessage("sql-injection.8.one")))))
                .andExpect(jsonPath("$.output", containsString("<table><tr><th>")));
    }

    @Test
    public void multipleAccounts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack8")
                .param("name", "Smith")
                .param("auth_tan", "3SL99A' OR '1' = '1"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", is(modifySpan(messages.getMessage("sql-injection.8.success")))))
                .andExpect(jsonPath("$.output", containsString("<tr><td>96134<\\/td><td>Bob<\\/td><td>Franco<\\/td><td>Marketing<\\/td><td>83700<\\/td><td>LO9S2V<\\/td><\\/tr>")));
    }

    @Test
    public void wrongNameReturnsNoAccounts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack8")
                .param("name", "Smithh")
                .param("auth_tan", "3SL99A"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(modifySpan(messages.getMessage("sql-injection.8.no.results")))))
                .andExpect(jsonPath("$.output").doesNotExist());
    }

    @Test
    public void wrongTANReturnsNoAccounts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack8")
                .param("name", "Smithh")
                .param("auth_tan", ""))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(modifySpan(messages.getMessage("sql-injection.8.no.results")))))
                .andExpect(jsonPath("$.output").doesNotExist());
    }

    @Test
    public void malformedQueryReturnsError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack8")
                .param("name", "Smith")
                .param("auth_tan", "3SL99A' OR '1' = '1'"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(modifySpan(messages.getMessage("sql-injection.error")))))
                .andExpect(jsonPath("$.output", containsString("feedback-negative")));
    }

    public static String modifySpan(String message) {
        return message.replace("</span>", "<\\/span>");
    }
}