package org.owasp.webgoat.lessons.missing_ac;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.lessons.missing_ac.DisplayUser;
import org.owasp.webgoat.lessons.missing_ac.MissingFunctionAC;
import org.owasp.webgoat.lessons.missing_ac.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.owasp.webgoat.lessons.missing_ac.MissingFunctionAC.PASSWORD_SALT_ADMIN;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MissingFunctionACYourHashAdminTest extends LessonTest {

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(new MissingFunctionAC());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void solve() throws Exception {
        var userHash = new DisplayUser(new User("Jerry", "doesnotreallymatter", true), PASSWORD_SALT_ADMIN).getUserHash();
        mockMvc.perform(MockMvcRequestBuilders.post("/access-control/user-hash-fix")
                .param("userHash", userHash))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("Congrats! You really succeeded when you added the user.")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    void wrongUserHash() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/access-control/user-hash-fix")
                .param("userHash", "wrong"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}
