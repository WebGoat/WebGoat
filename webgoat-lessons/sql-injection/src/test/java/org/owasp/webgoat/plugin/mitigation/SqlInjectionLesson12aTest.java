package org.owasp.webgoat.plugin.mitigation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugin.introduction.SqlInjection;
import org.owasp.webgoat.plugins.LessonTest;
import org.owasp.webgoat.session.WebgoatContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 5/21/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson12aTest extends LessonTest {

    @Autowired
    private WebgoatContext context;

    @Before
    public void setup()  {
        SqlInjection sql = new SqlInjection();

        when(webSession.getCurrentLesson()).thenReturn(sql);
        when(webSession.getWebgoatContext()).thenReturn(context);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void knownAccountShouldDisplayData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "id"))

                .andExpect(status().isOk());
    }

    @Test
    public void addressCorrectShouldOrderByHostname() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "CASE WHEN (SELECT ip FROM servers WHERE hostname='webgoat-prd') LIKE '104.%' THEN hostname ELSE id END"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));
    }

    @Test
    public void addressCorrectShouldOrderByHostnameUsingSubstr() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "case when (select ip from servers where hostname='webgoat-prd' and substr(ip,1,1) = '1') IS NOT NULL then hostname else id end"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));

        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "case when (select ip from servers where hostname='webgoat-prd' and substr(ip,2,1) = '0') IS NOT NULL then hostname else id end"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));

        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "case when (select ip from servers where hostname='webgoat-prd' and substr(ip,3,1) = '4') IS NOT NULL then hostname else id end"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));
    }

    @Test
    public void addressIncorrectShouldOrderByIdUsingSubstr() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "case when (select ip from servers where hostname='webgoat-prd' and substr(ip,1,1) = '9') IS NOT NULL then hostname else id end"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-dev")));
    }

    @Test
    public void trueShouldSortByHostname() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "(case when (true) then hostname else id end)"))

                .andExpect(status().isOk())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));
    }

    @Test
    public void falseShouldSortById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "(case when (true) then hostname else id end)"))

                .andExpect(status().isOk())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));
    }

    @Test
    public void addressIncorrectShouldOrderByHostname() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjection/servers")
                .param("column", "CASE WHEN (SELECT ip FROM servers WHERE hostname='webgoat-prd') LIKE '192.%' THEN hostname ELSE id END"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-dev")));
    }

    @Test
    public void postingCorrectAnswerShouldPassTheLesson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack12a")
                .param("ip", "104.130.219.202"))

                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void postingWrongAnswerShouldNotPassTheLesson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack12a")
                .param("ip", "192.168.219.202"))

                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }
}