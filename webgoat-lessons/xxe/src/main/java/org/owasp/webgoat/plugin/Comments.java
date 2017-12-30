package org.owasp.webgoat.plugin;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
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
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
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

    private static final Map<String, EvictingQueue<Comment>> userComments = Maps.newHashMap();
    private static final EvictingQueue<Comment> comments = EvictingQueue.create(100);

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
            EvictingQueue<Comment> comments = userComments.getOrDefault(webSession.getUserName(), EvictingQueue.create(100));
            comments.add(comment);
            userComments.put(webSession.getUserName(), comments);
        }
    }
}
