package org.owasp.webgoat.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author nbaars
 * @since 5/4/17.
 */
@RestController
@RequestMapping("xxe/comments")
public class CommentsEndpoint {

    @Autowired
    private Comments comments;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Comment> retrieveComments() {
        return comments.getComments();
    }

}
