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

package org.owasp.webgoat.lessons.xxe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.owasp.webgoat.container.session.WebSession;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
@Scope("singleton")
public class CommentsCache {

    static class Comments extends ArrayList<Comment> {
        void sort() {
            sort(Comparator.comparing(Comment::getDateTime).reversed());
        }
    }

    private static final Comments comments = new Comments();
    private static final Map<WebGoatUser, Comments> userComments = new HashMap<>();
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd, HH:mm:ss");

    private final WebSession webSession;

    public CommentsCache(WebSession webSession) {
        this.webSession = webSession;
        initDefaultComments();
    }

    void initDefaultComments() {
        comments.add(new Comment("webgoat", DateTime.now().toString(fmt), "Silly cat...."));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "I think I will use this picture in one of my projects."));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "Lol!! :-)."));
    }

    protected Comments getComments() {
        Comments allComments = new Comments();
        Comments commentsByUser = userComments.get(webSession.getUser());
        if (commentsByUser != null) {
            allComments.addAll(commentsByUser);
        }
        allComments.addAll(comments);
        allComments.sort();
        return allComments;
    }

    /**
     * Notice this parse method is not a "trick" to get the XXE working, we need to catch some of the exception which
     * might happen during when users post message (we want to give feedback track progress etc). In real life the
     * XmlMapper bean defined above will be used automatically and the Comment class can be directly used in the
     * controller method (instead of a String)
     */
    protected Comment parseXml(String xml) throws JAXBException, XMLStreamException {
        var jc = JAXBContext.newInstance(Comment.class);
        var xif = XMLInputFactory.newInstance();

        if (webSession.isSecurityEnabled()) {
            xif.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
            xif.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");  // compliant
        }

        var xsr = xif.createXMLStreamReader(new StringReader(xml));

        var unmarshaller = jc.createUnmarshaller();
        return (Comment) unmarshaller.unmarshal(xsr);
    }

    protected Optional<Comment> parseJson(String comment) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return of(mapper.readValue(comment, Comment.class));
        } catch (IOException e) {
            return empty();
        }
    }

    public void addComment(Comment comment, boolean visibleForAllUsers) {
        comment.setDateTime(DateTime.now().toString(fmt));
        comment.setUser(webSession.getUserName());
        if (visibleForAllUsers) {
            comments.add(comment);
        } else {
            var comments = userComments.getOrDefault(webSession.getUserName(), new Comments());
            comments.add(comment);
            userComments.put(webSession.getUser(), comments);
        }
    }

    public void reset(WebGoatUser user) {
        comments.clear();
        userComments.remove(user);
        initDefaultComments();
    }
}
