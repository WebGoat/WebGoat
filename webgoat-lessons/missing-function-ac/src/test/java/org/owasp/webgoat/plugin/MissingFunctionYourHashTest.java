package org.owasp.webgoat.plugin;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.users.UserService;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class MissingFunctionYourHashTest extends AssignmentEndpointTest {
    private MockMvc mockMvc;
    private DisplayUser mockDisplayUser;

    @Mock
    protected UserService userService;

    @Before
    public void setUp() {
        MissingFunctionACYourHash yourHashTest = new MissingFunctionACYourHash();
        init(yourHashTest);
        this.mockMvc = standaloneSetup(yourHashTest).build();
        this.mockDisplayUser = new DisplayUser(new WebGoatUser("user","userPass"));
        ReflectionTestUtils.setField(yourHashTest,"userService",userService);
        when(mockDisplayUser.getUserHash()).thenReturn("2340928sadfajsdalsNfwrBla=");
        when(userService.loadUserByUsername(anyString())).thenReturn(new WebGoatUser("user","userPass"));
    }

    @Test
    public void HashDoesNotMatch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/access-control/user-hash")
                .param("userHash", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("Keep trying, this one may take several attempts")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void hashMatches() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/access-control/user-hash")
                .param("userHash", "2340928sadfajsdalsNfwrBla="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("Keep trying, this one may take several attempts")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

}
