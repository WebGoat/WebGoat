package org.owasp.webgoat.plugin;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.owasp.webgoat.plugin.ClientSideFilteringFreeAssignment.SUPER_COUPON_CODE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author nbaars
 * @since 5/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ClientSideFilteringFreeAssignmentTest extends LessonTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        ClientSideFiltering clientSideFiltering = new ClientSideFiltering();
        when(webSession.getCurrentLesson()).thenReturn(clientSideFiltering);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clientSideFiltering/getItForFree")
                .param("checkoutCode", SUPER_COUPON_CODE))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void wrongCouponCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clientSideFiltering/getItForFree")
                .param("checkoutCode", "test"))
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}