/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.xss.stored;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.xss.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;

import java.io.IOException;
import java.util.*;


@RestController
public class StoredXssComments extends AssignmentEndpoint {

    @Autowired
    private WebSession webSession;
    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd, HH:mm:ss");

    private static final Map<String, List<Comment>> userComments = new HashMap<>();
    private static final List<Comment> comments = new ArrayList<>();
    private static final String phoneHomeString = "<script>webgoat.customjs.phoneHome()</script>";


    static {
        comments.add(new Comment("secUriTy", DateTime.now().toString(fmt), "<script>console.warn('unit test me')</script>Comment for Unit Testing"));
        comments.add(new Comment("webgoat", DateTime.now().toString(fmt), "This comment is safe"));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "This one is safe too."));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "Can you post a comment, calling webgoat.customjs.phoneHome() ?"));
    }

    //TODO This assignment seems not to be in use in the UI
    @GetMapping(path = "/CrossSiteScriptingStored/stored-xss", produces = MediaType.APPLICATION_JSON_VALUE, consumes = ALL_VALUE)
    @ResponseBody
    public Collection<Comment> retrieveComments() {
        List<Comment> allComments = Lists.newArrayList();
        Collection<Comment> newComments = userComments.get(webSession.getUserName());
        allComments.addAll(comments);
        if (newComments != null) {
            allComments.addAll(newComments);
        }
        Collections.reverse(allComments);
        return allComments;
    }

    //TODO This assignment seems not to be in use in the UI
    @PostMapping("/CrossSiteScriptingStored/stored-xss")
    @ResponseBody
    public AttackResult createNewComment(@RequestBody String commentStr) {
        Comment comment = parseJson(commentStr);

        List<Comment> comments = userComments.getOrDefault(webSession.getUserName(), new ArrayList<>());
        comment.setDateTime(DateTime.now().toString(fmt));
        comment.setUser(webSession.getUserName());

        comments.add(comment);
        userComments.put(webSession.getUserName(), comments);

        if (comment.getText().contains(phoneHomeString)) {
            return (success(this).feedback("xss-stored-comment-success").build());
        } else {
            return (failed(this).feedback("xss-stored-comment-failure").build());
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