package org.owasp.webgoat.plugin.challenge2;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.plugin.Flag;
import org.owasp.webgoat.plugin.SolutionConstants;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author nbaars
 * @since 5/2/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class Assignment2Test extends AssignmentEndpointTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        Assignment2 assignment2 = new Assignment2();
        init(assignment2);
        new Flag().initFlags();
        this.mockMvc = standaloneSetup(assignment2).build();
    }

    @Test
    public void success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/2")
                .param("checkoutCode", SolutionConstants.SUPER_COUPON_CODE))
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("flag: " + Flag.FLAGS.get(2))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void wrongCouponCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/2")
                .param("checkoutCode", "test"))
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}