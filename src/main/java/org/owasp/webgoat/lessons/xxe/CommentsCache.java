/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
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
