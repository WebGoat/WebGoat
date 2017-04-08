package org.owasp.webgoat.plugin.challenge3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.EvictingQueue;
import org.joda.time.DateTime;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.Flag;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@AssignmentPath("/challenge/3")
public class Challenge3 extends AssignmentEndpoint {

    @Autowired
    private WebSession webSession;
    private static final EvictingQueue<Comment> comments = EvictingQueue.create(100);

    static {
        comments.add(new Comment("webgoat", DateTime.now().toString(), "Silly cat...."));
        comments.add(new Comment("guest", DateTime.now().toString(), "I think I will use this picture in one of my projects."));
        comments.add(new Comment("guest", DateTime.now().toString(), "Lol!! :-)."));
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Comment> retrieveComments() {
        return comments;
    }

    @RequestMapping(method = POST, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult createNewUser(@RequestBody String commentStr, @RequestHeader("Content-Type") String contentType) throws Exception {
        Comment comment = new Comment();
        AttackResult attackResult = failed().build();
        if (APPLICATION_JSON_VALUE.equals(contentType)) {
            comment = parseJson(commentStr);
            comment.setDateTime(DateTime.now().toString());
            comment.setUser(webSession.getUserName());
        }
        if (MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
            comment = parseXml(commentStr);
            comment.setDateTime(DateTime.now().toString());
            comment.setUser(webSession.getUserName());
        }

        if (checkSolution(comment)) {
            attackResult = success().feedback("challenge.solved").feedbackArgs(Flag.FLAGS.get(2)).build();
        }
        return attackResult;
    }

    private boolean checkSolution(Comment comment) {
        if (comment.getComment().contains("Congratulations you may now collect your flag")) {
            comment.setComment("Congratulations to " + webSession.getUserName() + " for finding the flag!!");
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

