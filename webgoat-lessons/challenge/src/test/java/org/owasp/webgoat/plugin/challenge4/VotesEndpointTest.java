package org.owasp.webgoat.plugin.challenge4;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.plugin.Flag;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author nbaars
 * @since 5/2/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class VotesEndpointTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        VotesEndpoint votesEndpoint = new VotesEndpoint();
        votesEndpoint.initVotes();
        new Flag().initFlags();
        this.mockMvc = standaloneSetup(votesEndpoint).build();
    }

    @Test
    public void loginWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/login")
                .param("user", "uknown"))
                .andExpect(unauthenticated());
    }

    @Test
    public void loginWithTomShouldGiveJwtToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/login")
                .param("user", "Tom"))
                .andExpect(status().isOk()).andExpect(cookie().exists("access_token"));
    }

    @Test
    public void loginWithGuestShouldNotGiveJwtToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/login")
                .param("user", "Guest"))
                .andExpect(unauthenticated()).andExpect(cookie().value("access_token", ""));
    }

    @Test
    public void userShouldSeeMore() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/votings/login")
                .param("user", "Tom"))
                .andExpect(status().isOk()).andExpect(cookie().exists("access_token")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/votings")
                .cookie(mvcResult.getResponse().getCookie("access_token")))
                .andExpect(jsonPath("$.[*].numberOfVotes").exists());
    }

    @Test
    public void guestShouldNotSeeNumberOfVotes() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/votings/login")
                .param("user", "Guest"))
                .andExpect(unauthenticated()).andExpect(cookie().exists("access_token")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/votings")
                .cookie(mvcResult.getResponse().getCookie("access_token")))
                .andExpect(jsonPath("$.[*].numberOfVotes").doesNotExist());
    }

    @Test
    public void adminShouldSeeFlags() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings")
                .cookie(new Cookie("access_token", "eyJhbGciOiJub25lIn0.eyJhZG1pbiI6InRydWUiLCJ1c2VyIjoiSmVycnkifQ.")))
                .andExpect(jsonPath("$.[*].flag").isNotEmpty());
    }

    @Test
    public void votingIsNotAllowedAsGuest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/votings/Get it for free"))
                .andExpect(unauthenticated());
    }

    @Test
    public void normalUserShouldBeAbleToVote() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/votings/login")
                .param("user", "Tom"))
                .andExpect(status().isOk()).andExpect(cookie().exists("access_token")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/votings/Get it for free")
                .cookie(mvcResult.getResponse().getCookie("access_token")));
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/")
                .cookie(mvcResult.getResponse().getCookie("access_token")))
                .andExpect(jsonPath("$..[?(@.title == 'Get it for free')].numberOfVotes", CoreMatchers.hasItem(20001)));
    }

    @Test
    public void votingForUnknownLessonShouldNotCrash() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/votings/login")
                .param("user", "Tom"))
                .andExpect(status().isOk()).andExpect(cookie().exists("access_token")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/votings/UKNOWN_VOTE")
                .cookie(mvcResult.getResponse().getCookie("access_token"))).andExpect(status().isAccepted());
    }

    @Test
    public void votingWithInvalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/votings/UKNOWN_VOTE")
                .cookie(new Cookie("access_token", "abc"))).andExpect(unauthenticated());
    }

    @Test
    public void gettingVotesWithInvalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/")
                .cookie(new Cookie("access_token", "abc"))).andExpect(unauthenticated());
    }

    @Test
    public void gettingVotesWithUnknownUserInToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/")
                .cookie(new Cookie("access_token", "eyJhbGciOiJub25lIn0.eyJhZG1pbiI6InRydWUiLCJ1c2VyIjoiVW5rbm93biJ9.")))
                .andExpect(unauthenticated())
                .andExpect(jsonPath("$.[*].numberOfVotes").doesNotExist());
    }

    @Test
    public void gettingVotesForUnknownShouldWork() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/")
                .cookie(new Cookie("access_token", "eyJhbGciOiJub25lIn0.eyJ1c2VyIjoiVW5rbm93biJ9.")))
                .andExpect(unauthenticated())
                .andExpect(jsonPath("$.[*].numberOfVotes").doesNotExist());
    }

    @Test
    public void gettingVotesForKnownWithoutAdminFieldShouldWork() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/")
                .cookie(new Cookie("access_token", "eyJhbGciOiJub25lIn0.eyJ1c2VyIjoiVG9tIn0.")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].numberOfVotes").exists());
    }

    @Test
    public void gettingVotesWithEmptyToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/votings/")
                .cookie(new Cookie("access_token", "")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].numberOfVotes").doesNotExist());
    }

    @Test
    public void votingAsUnknownUserShouldNotBeAllowed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/votings/Get it for free")
                .cookie(new Cookie("access_token", "eyJhbGciOiJub25lIn0.eyJ1c2VyIjoiVW5rbm93biJ9.")))
                .andExpect(unauthenticated());
    }
}