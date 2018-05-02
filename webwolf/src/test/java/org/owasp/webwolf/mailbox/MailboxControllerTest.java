package org.owasp.webwolf.mailbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MailboxController.class)
public class MailboxControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private MailboxRepository mailbox;
    @Autowired
    private ObjectMapper objectMapper;

    @JsonIgnoreProperties("time")
    public static class EmailMixIn {
    }

    @Before
    public void setup() {
        objectMapper.addMixIn(Email.class, EmailMixIn.class);
    }

    @Test
    @WithMockUser
    public void sendingMailShouldStoreIt() throws Exception {
        Email email = Email.builder()
                .contents("This is a test mail")
                .recipient("test1234@webgoat.org")
                .sender("hacker@webgoat.org")
                .title("Click this mail")
                .time(LocalDateTime.now())
                .build();
        this.mvc.perform(post("/mail").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(email)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test1234")
    public void userShouldBeAbleToReadOwnEmail() throws Exception {
        Email email = Email.builder()
                .contents("This is a test mail")
                .recipient("test1234@webgoat.org")
                .sender("hacker@webgoat.org")
                .title("Click this mail")
                .time(LocalDateTime.now())
                .build();
        Mockito.when(mailbox.findByRecipientOrderByTimeDesc("test1234")).thenReturn(Lists.newArrayList(email));

        this.mvc.perform(get("/WebWolf/mail"))
                .andExpect(status().isOk())
                .andExpect(view().name("mailbox"))
                .andExpect(content().string(containsString("Click this mail")))
                .andExpect(content().string(containsString(DateTimeFormatter.ofPattern("h:mm a").format(email.getTimestamp()))));
    }

    @Test
    @WithMockUser(username = "test1233")
    public void differentUserShouldNotBeAbleToReadOwnEmail() throws Exception {
        Email email = Email.builder()
                .contents("This is a test mail")
                .recipient("test1234@webgoat.org")
                .sender("hacker@webgoat.org")
                .title("Click this mail")
                .time(LocalDateTime.now())
                .build();
        Mockito.when(mailbox.findByRecipientOrderByTimeDesc("test1234")).thenReturn(Lists.newArrayList(email));

        this.mvc.perform(get("/WebWolf/mail"))
                .andExpect(status().isOk())
                .andExpect(view().name("mailbox"))
                .andExpect(content().string(not(containsString("Click this mail"))));
    }

}