package org.owasp.webgoat.plugin;

import com.google.common.collect.Maps;
import org.owasp.webgoat.assignments.Endpoint;
import org.owasp.webgoat.session.UserTracker;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @author nbaars
 * @since 3/23/17.
 */
public class Flag extends Endpoint {

    public static final Map<Integer, String> FLAGS = Maps.newHashMap();
    @Autowired
    private UserTracker userTracker;
    @Autowired
    private WebSession webSession;

    @PostConstruct
    public void initFlags() {
        IntStream.range(1, 4).forEach(i -> FLAGS.put(i, UUID.randomUUID().toString()));
    }

    @Override
    public String getPath() {
        return "challenge/flag";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void postFlag(@RequestParam String flag, @RequestParam int challengeNumber) {
        String expectedFlag = FLAGS.get(challengeNumber);
        if (expectedFlag.equals(flag)) {
            userTracker.assignmentSolved(webSession.getCurrentLesson(), "Challenge" + challengeNumber);
        } else {
            userTracker.assignmentFailed(webSession.getCurrentLesson());
        }
    }

}
