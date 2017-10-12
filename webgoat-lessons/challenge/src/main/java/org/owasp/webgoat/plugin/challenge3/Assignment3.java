package org.owasp.webgoat.plugin.challenge3;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.Flag;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@AssignmentPath("/challenge/3")
@Slf4j
public class Assignment3 extends AssignmentEndpoint {

    @Value("${webgoat.server.directory}")
    private String webGoatHomeDirectory;
    @Autowired
    private WebSession webSession;
    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd, HH:mm:ss");

    private static final Map<String, EvictingQueue<Comment>> userComments = Maps.newHashMap();
    private static final EvictingQueue<Comment> comments = EvictingQueue.create(100);
    private static final String secretContents = "Congratulations you may now collect your flag";

    static {
        comments.add(new Comment("webgoat", DateTime.now().toString(fmt), "Silly cat...."));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "I think I will use this picture in one of my projects."));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "Lol!! :-)."));
    }

    @PostConstruct
    @SneakyThrows
    public void copyFile() {
        File targetDirectory = new File(webGoatHomeDirectory);
        if (!targetDirectory.exists()) {
            targetDirectory.mkdir();
        }
        log.info("Copied secret.txt to: {}", targetDirectory);
        Files.write(secretContents, new File(targetDirectory, "secret.txt"), Charset.defaultCharset());
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Comment> retrieveComments() {
        Collection<Comment> allComments = Lists.newArrayList();
        Collection<Comment> xmlComments = userComments.get(webSession.getUserName());
        if (xmlComments != null) {
            allComments.addAll(xmlComments);
        }
        allComments.addAll(comments);
        return allComments;
    }

    @RequestMapping(method = POST, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult createNewComment(@RequestBody String commentStr, @RequestHeader("Content-Type") String contentType) throws Exception {
        Comment comment = null;
        AttackResult attackResult = failed().build();
        if (APPLICATION_JSON_VALUE.equals(contentType)) {
            comment = parseJson(commentStr);
            comment.setDateTime(DateTime.now().toString(fmt));
            comment.setUser(webSession.getUserName());
            comments.add(comment);
        }
        if (MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
            //Do not show these comments to all users
            comment = parseXml(commentStr);
            comment.setDateTime(DateTime.now().toString(fmt));
            comment.setUser(webSession.getUserName());
            EvictingQueue<Comment> comments = userComments.getOrDefault(webSession.getUserName(), EvictingQueue.create(100));
            comments.add(comment);
            userComments.put(webSession.getUserName(), comments);
        }
        if (checkSolution(comment)) {
            attackResult = success().feedback("challenge.solved").feedbackArgs(Flag.FLAGS.get(3)).build();
        }
        return attackResult;
    }

    private boolean checkSolution(Comment comment) {
        if (comment.getText().contains(secretContents)) {
            comment.setText("Congratulations to " + webSession.getUserName() + " for finding the flag!! Check your original response where you posted the XXE attack ");
            comments.add(comment);
            return true;
        }
        return false;
    }

    public static Comment parseXml(String xml) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Comment.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        xif.setProperty(XMLInputFactory.IS_VALIDATING, false);

        xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(xml));

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (Comment) unmarshaller.unmarshal(xsr);
    }

    private Comment parseJson(String comment) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(comment, Comment.class);
        } catch (IOException e) {
            return new Comment();
        }
    }


}

