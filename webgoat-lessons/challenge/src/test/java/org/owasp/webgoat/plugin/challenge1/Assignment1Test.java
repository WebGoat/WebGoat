package org.owasp.webgoat.plugin.challenge1;

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

import java.net.InetAddress;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author nbaars
 * @since 5/2/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class Assignment1Test extends AssignmentEndpointTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        Assignment1 assignment1 = new Assignment1();
        init(assignment1);
        new Flag().initFlags();
        this.mockMvc = standaloneSetup(assignment1).build();
    }

    @Test
    public void success() throws Exception {
        InetAddress addr = InetAddress.getLocalHost();
        String host = addr.getHostAddress();
        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/1")
                .header("X-Forwarded-For", host)
                .param("username", "admin")
                .param("password", SolutionConstants.PASSWORD))
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("flag: " + Flag.FLAGS.get(1))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void wrongPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/1")
                .param("username", "admin")
                .param("password", "wrong"))
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

//    @Test
//    public void correctPasswordXForwardHeaderMissing() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/1")
//                .param("username", "admin")
//                .param("password", SolutionConstants.PASSWORD))
//                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("ip.address.unknown"))))
//                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
//    }

//    @Test
//    public void correctPasswordXForwardHeaderWrong() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/1")
//                .header("X-Forwarded-For", "127.0.1.2")
//                .param("username", "admin")
//                .param("password", SolutionConstants.PASSWORD))
//                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("ip.address.unknown"))))
//                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
//    }

}