package org.owasp.webgoat.sql_injection.mitigation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.sql_injection.SqlLessonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 5/21/17.
 */
@ExtendWith(SpringExtension.class)
public class SqlInjectionLesson13Test extends SqlLessonTest {

    @Test
    public void knownAccountShouldDisplayData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "id"))

                .andExpect(status().isOk());
    }

    @Test
    public void addressCorrectShouldOrderByHostname() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "CASE WHEN (SELECT ip FROM servers WHERE hostname='webgoat-prd') LIKE '104.%' THEN hostname ELSE id END"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));
    }

    @Test
    public void addressCorrectShouldOrderByHostnameUsingSubstr() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "case when (select ip from servers where hostname='webgoat-prd' and substr(ip,1,1) = '1') IS NOT NULL then hostname else id end"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));

        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "case when (select ip from servers where hostname='webgoat-prd' and substr(ip,2,1) = '0') IS NOT NULL then hostname else id end"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));

        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "case when (select ip from servers where hostname='webgoat-prd' and substr(ip,3,1) = '4') IS NOT NULL then hostname else id end"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));
    }

    @Test
    public void addressIncorrectShouldOrderByIdUsingSubstr() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "case when (select ip from servers where hostname='webgoat-prd' and substr(ip,1,1) = '9') IS NOT NULL then hostname else id end"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-dev")));
    }

    @Test
    public void trueShouldSortByHostname() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "(case when (true) then hostname else id end)"))

                .andExpect(status().isOk())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));
    }

    @Test
    public void falseShouldSortById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "(case when (true) then hostname else id end)"))

                .andExpect(status().isOk())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-acc")));
    }

    @Test
    public void addressIncorrectShouldOrderByHostname() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/SqlInjectionMitigations/servers")
                .param("column", "CASE WHEN (SELECT ip FROM servers WHERE hostname='webgoat-prd') LIKE '192.%' THEN hostname ELSE id END"))

                .andExpect(status().isOk()).andExpect(jsonPath("$[0].hostname", is("webgoat-dev")));
    }

    @Test
    public void postingCorrectAnswerShouldPassTheLesson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjectionMitigations/attack12a")
                .param("ip", "104.130.219.202"))

                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void postingWrongAnswerShouldNotPassTheLesson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjectionMitigations/attack12a")
                .param("ip", "192.168.219.202"))

                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }
}