package org.owasp.webgoat.plugin;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 11/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleXXETest extends LessonTest {

    @Before
    public void setup() throws Exception {
        XXE xxe = new XXE();
        when(webSession.getCurrentLesson()).thenReturn(xxe);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void workingAttack() throws Exception {
        //Call with XXE injection
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/simple")
                .content("<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))));
    }

    @Test
    public void postingJsonCommentShouldNotSolveAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/simple")
                .content("<comment><text>test</ext></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
    }

    @Test
    public void postingXmlCommentWithoutXXEShouldNotSolveAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/simple")
                .content("<?xml version=\"1.0\" standalone=\"yes\" ?><comment><text>&root;</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
    }

    @Test
    public void postingPlainTextShouldShwoException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/simple")
                .content("test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output", CoreMatchers.startsWith("javax.xml.bind.UnmarshalException\\n - with linked exception")))
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
    }

}