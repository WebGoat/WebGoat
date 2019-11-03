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

package org.owasp.webgoat.xxe;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * @author nbaars
 * @since 5/3/17.
 */
@Component
@Scope("singleton")
public class Comments {

    @Autowired
    protected WebSession webSession;

    protected static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd, HH:mm:ss");

    private static final Map<String, List<Comment>> userComments = new HashMap<>();
    private static final List<Comment> comments = new ArrayList<>();

    static {
        comments.add(new Comment("webgoat", DateTime.now().toString(fmt), "Silly cat...."));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "I think I will use this picture in one of my projects."));
        comments.add(new Comment("guest", DateTime.now().toString(fmt), "Lol!! :-)."));
    }

    protected Collection<Comment> getComments() {
        Collection<Comment> allComments = Lists.newArrayList();
        Collection<Comment> xmlComments = userComments.get(webSession.getUserName());
        if (xmlComments != null) {
            allComments.addAll(xmlComments);
        }
        allComments.addAll(comments);
        return allComments.stream().sorted(Comparator.comparing(Comment::getDateTime).reversed()).collect(Collectors.toList());
    }

    protected Comment parseXml(String xml) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Comment.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        xif.setProperty(XMLInputFactory.IS_VALIDATING, false);

        xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(xml));

        Unmarshaller unmarshaller = jc.createUnmarshaller();
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
            List<Comment> comments = userComments.getOrDefault(webSession.getUserName(), new ArrayList<>());
            comments.add(comment);
            userComments.put(webSession.getUserName(), comments);
        }
    }
}
