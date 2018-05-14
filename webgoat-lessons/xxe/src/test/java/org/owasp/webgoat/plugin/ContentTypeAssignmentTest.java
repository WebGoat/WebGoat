package org.owasp.webgoat.plugin;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 11/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ContentTypeAssignmentTest extends LessonTest {

    @Autowired
    private Comments comments;

    @Before
    public void setup() throws Exception {
        XXE xxe = new XXE();
        when(webSession.getCurrentLesson()).thenReturn(xxe);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void sendingXmlButContentTypeIsJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content("<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));
    }

    @Test
    public void workingAttack() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_XML)
                .content("<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))));
    }

    @Test
    public void postingJsonShouldAddComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{  \"text\" : \"Hello World\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));
        assertThat(comments.getComments().stream().filter(c -> c.getText().equals("Hello World")).count()).isEqualTo(1);
    }

    @Test
    public void postingInvalidJsonShouldAddComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{  'text' : 'Wrong'"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));
        assertThat(comments.getComments().stream().filter(c -> c.getText().equals("Wrong")).count()).isEqualTo(0);
    }

}