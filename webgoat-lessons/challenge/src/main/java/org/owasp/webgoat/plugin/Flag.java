package org.owasp.webgoat.plugin;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.assignments.Endpoint;
import org.owasp.webgoat.i18n.PluginMessages;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    private PluginMessages pluginMessages;

    @AllArgsConstructor
    private class FlagPosted {
        @Getter
        private boolean lessonCompleted;
    }

    @PostConstruct
    public void initFlags() {
        IntStream.range(1, 10).forEach(i -> FLAGS.put(i, UUID.randomUUID().toString()));
    }

    @Override
    public String getPath() {
        return "challenge/flag";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult postFlag(@RequestParam String flag) {
        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        String currentChallenge = webSession.getCurrentLesson().getName();
        int challengeNumber = Integer.valueOf(currentChallenge.substring(currentChallenge.length() - 1, currentChallenge.length()));
        String expectedFlag = FLAGS.get(challengeNumber);
        final AttackResult attackResult;
        if (expectedFlag.equals(flag)) {
            userTracker.assignmentSolved(webSession.getCurrentLesson(), "Assignment" + challengeNumber);
            attackResult = new AttackResult.AttackResultBuilder(pluginMessages).lessonCompleted(true, "challenge.flag.correct").build();
        } else {
            userTracker.assignmentFailed(webSession.getCurrentLesson());
            attackResult = new AttackResult.AttackResultBuilder(pluginMessages).feedback("challenge.flag.incorrect").build();
        }
        userTrackerRepository.save(userTracker);
        return attackResult;
    }
}
