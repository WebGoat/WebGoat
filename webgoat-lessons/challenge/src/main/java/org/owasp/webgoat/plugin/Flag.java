package org.owasp.webgoat.plugin;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.Endpoint;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Slf4j
public class Flag extends Endpoint {

    public static final Map<Integer, String> FLAGS = Maps.newHashMap();
    @Autowired
    private UserTrackerRepository userTrackerRepository;
    @Autowired
    private WebSession webSession;

    @AllArgsConstructor
    private class FlagPosted {
        @Getter
        private boolean lessonCompleted;
    }

    @PostConstruct
    public void initFlags() {
        IntStream.range(1, 6).forEach(i -> FLAGS.put(i, UUID.randomUUID().toString()));
        FLAGS.entrySet().stream().forEach(e ->  log.debug("Flag {} {}", e.getKey(), e.getValue()));
    }

    @Override
    public String getPath() {
        return "challenge/flag";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void postFlag(@RequestParam String flag) {
        UserTracker userTracker = userTrackerRepository.findOne(webSession.getUserName());
        if (userTracker == null) {
            userTracker = new UserTracker(webSession.getUserName());
        }
        String currentChallenge = webSession.getCurrentLesson().getName();
        int challengeNumber = Integer.valueOf(currentChallenge.substring(currentChallenge.length() - 1, currentChallenge.length()));
        String expectedFlag = FLAGS.get(challengeNumber);
        if (expectedFlag.equals(flag)) {
            userTracker.assignmentSolved(webSession.getCurrentLesson(), "Assignment" + challengeNumber);
        } else {
            userTracker.assignmentFailed(webSession.getCurrentLesson());
        }
        userTrackerRepository.save(userTracker);
    }

}
