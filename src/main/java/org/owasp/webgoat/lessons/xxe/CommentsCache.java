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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
  private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");

  public CommentsCache() {
    initDefaultComments();
  }

  void initDefaultComments() {
    comments.add(new Comment("webgoat", LocalDateTime.now().format(fmt), "Silly cat...."));
    comments.add(
        new Comment(
            "guest",
            LocalDateTime.now().format(fmt),
            "I think I will use this picture in one of my projects."));
    comments.add(new Comment("guest", LocalDateTime.now().format(fmt), "Lol!! :-)."));
  }

  protected Comments getComments(WebGoatUser user) {
    Comments allComments = new Comments();
    Comments commentsByUser = userComments.get(user);
    if (commentsByUser != null) {
      allComments.addAll(commentsByUser);
    }
    allComments.addAll(comments);
    allComments.sort();
    return allComments;
  }

  /**
   * Notice this parse method is not a "trick" to get the XXE working, we need to catch some of the
   * exception which might happen during when users post message (we want to give feedback track
   * progress etc). In real life the XmlMapper bean defined above will be used automatically and the
   * Comment class can be directly used in the controller method (instead of a String)
   */
  protected Comment parseXml(String xml, boolean securityEnabled)
      throws XMLStreamException, JAXBException {
    var jc = JAXBContext.newInstance(Comment.class);
    var xif = XMLInputFactory.newInstance();

    // TODO fix me disabled for now.
    if (securityEnabled) {
      xif.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
      xif.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
    }

    var xsr = xif.createXMLStreamReader(new StringReader(xml));

    var unmarshaller = jc.createUnmarshaller();
    return (Comment) unmarshaller.unmarshal(xsr);
  }

  public void addComment(Comment comment, WebGoatUser user, boolean visibleForAllUsers) {
    comment.setDateTime(LocalDateTime.now().format(fmt));
    comment.setUser(user.getUsername());
    if (visibleForAllUsers) {
      comments.add(comment);
    } else {
      var comments = userComments.getOrDefault(user.getUsername(), new Comments());
      comments.add(comment);
      userComments.put(user, comments);
    }
  }

  public void reset(WebGoatUser user) {
    comments.clear();
    userComments.remove(user);
    initDefaultComments();
  }
}
