package org.owasp.webgoat.bypass_restrictions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 6/16/17.
 */
@ExtendWith(SpringExtension.class)
public class BypassRestrictionsFrontendValidationTest extends LessonTest {

    @Autowired
    private BypassRestrictions bypassRestrictions;

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(bypassRestrictions);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void noChangesShouldNotPassTheLesson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/BypassRestrictions/frontendValidation")
                .param("field1", "abc")
                .param("field2", "123")
                .param("field3", "abc ABC 123")
                .param("field4", "seven")
                .param("field5", "01101")
                .param("field6", "90201 1111")
                .param("field7", "301-604-4882")
                .param("error", "2"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }

    @Test
    void bypassAllFieldShouldPass() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/BypassRestrictions/frontendValidation")
                .param("field1", "abcd")
                .param("field2", "1234")
                .param("field3", "abc $ABC 123")
                .param("field4", "ten")
                .param("field5", "01101AA")
                .param("field6", "90201 1111AA")
                .param("field7", "301-604-4882$$")
                .param("error", "0"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    void notBypassingAllFieldShouldNotPass() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/BypassRestrictions/frontendValidation")
                .param("field1", "abc")
                .param("field2", "1234")
                .param("field3", "abc $ABC 123")
                .param("field4", "ten")
                .param("field5", "01101AA")
                .param("field6", "90201 1111AA")
                .param("field7", "301-604-4882AA")
                .param("error", "0"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }


}