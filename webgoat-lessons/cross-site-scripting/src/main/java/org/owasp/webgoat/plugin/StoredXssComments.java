package org.owasp.webgoat.plugin;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by jason on 11/23/16.
 */

@AssignmentPath("/CrossSiteScripting/stored-xss")
public class StoredXssComments extends AssignmentEndpoint {

    @Autowired
    private WebSession webSession;
    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd, HH:mm:ss");

    private static final Map<String, EvictingQueue<Comment>> userComments = Maps.newHashMap();
    private static final EvictingQueue<Comment> comments = EvictingQueue.create(100);
    private static final String phoneHomeString = "<script>webgoat.customjs.phoneHome()</script>";

    static {
        comments.add(new Comment("webgoat", DateTime.now().toString(fmt), "This comment is safe"));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "This one is safe too."));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "Can you post a comment,  calling webgoat.customjs.phoneHome() ?"));
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE,consumes = ALL_VALUE)
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

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult createNewComment (@RequestBody String commentStr)  throws IOException {

        Comment comment = parseJson(commentStr);

        EvictingQueue<Comment> comments = userComments.getOrDefault(webSession.getUserName(), EvictingQueue.create(100));
        comments.add(comment);
        comment.setDateTime(DateTime.now().toString(fmt));
        comment.setUser(webSession.getUserName());

        userComments.put(webSession.getUserName(), comments);

        if (comment.getText().contains(phoneHomeString)) {
            return (success().feedback("xss-stored-comment-success").build());
        } else {
            return (failed().feedback("xss-stored-comment-failure").build());
        }
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





