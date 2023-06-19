package org.owasp.webgoat.lessons.idor;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.container.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class IDOREditOtherProfiileTest extends LessonTest {
  @BeforeEach
  public void setup() {
    Mockito.when(webSession.getCurrentLesson()).thenReturn(new IDOR());
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    Mockito.when(webSession.getUserName()).thenReturn("unit-test");
  }

  private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private UserSessionData userSessionData;

  @Test
  void solveRed() throws Exception {
      userSessionData.setValue("idor-authenticated-user-id", ""
    UserProfile userProfile = new UserProfile("2342384");
    userProfile.setRole(0);
    userProfile.setColor("red");
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/IDOR/profile/{userId}", "2342388")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userProfile)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void solveBlack() throws Exception {
    UserProfile userProfile = new UserProfile("2342384");
    userProfile.setRole(0);
    userProfile.setColor("black");
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/IDOR/profile/{userId}", "2342388")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userProfile)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

    @Test
    void roleNotChanged() throws Exception {
        UserProfile userProfile = new UserProfile("2342384");
        userProfile.setRole(2);
        userProfile.setColor("red");
        mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/IDOR/profile/{userId}", "2342388")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.feedback", CoreMatchers.is(messages.getMessage("idor.edit.profile.failure1"))));
    }

    @Test
    void colorNotChanged() throws Exception {
        UserProfile userProfile = new UserProfile("2342384");
        userProfile.setRole(1);
        userProfile.setColor("black");
        mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/IDOR/profile/{userId}", "2342388")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.feedback", CoreMatchers.is(messages.getMessage("idor.edit.profile.failure2"))));
    }

    @Test
    void wrongColorAndRoleUsed() throws Exception {
        UserProfile userProfile = new UserProfile("2342388");
        userProfile.setRole(2);
        userProfile.setColor("purple");
        mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/IDOR/profile/{userId}", "2342388")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.feedback", CoreMatchers.is(messages.getMessage("idor.edit.profile.failure3"))));
    }


    @Test
  void wrongUserId() throws Exception {
    UserProfile userProfile = new UserProfile("2342384");
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/IDOR/profile/{userId}", "2342388")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userProfile)))
        .andExpect(
            jsonPath(
                "$.feedback", CoreMatchers.is(messages.getMessage("idor.edit.profile.failure3"))));
  }

  @Test
  void noUserId() throws Exception {
    UserProfile userProfile = new UserProfile();
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/IDOR/profile/{userId}", "2342388")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userProfile)))
        .andExpect(
            jsonPath(
                "$.feedback", CoreMatchers.is(messages.getMessage("idor.edit.profile.failure3"))));
  }
}
