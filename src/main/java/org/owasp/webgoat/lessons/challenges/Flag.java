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

package org.owasp.webgoat.lessons.challenges;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.WebSession;
import org.owasp.webgoat.container.users.UserTracker;
import org.owasp.webgoat.container.users.UserTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @author nbaars
 * @since 3/23/17.
 */
@RestController
public class Flag extends AssignmentEndpoint {

    public static final Map<Integer, String> FLAGS = new HashMap<>();
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
        IntStream.range(1, 10).forEach(i -> FLAGS.put(i, UUID.randomUUID().toString()));
    }

    @RequestMapping(path = "/challenge/flag", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult postFlag(@RequestParam String flag) {
        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        String currentChallenge = webSession.getCurrentLesson().getName();
        int challengeNumber = Integer.valueOf(currentChallenge.substring(currentChallenge.length() - 1, currentChallenge.length()));
        String expectedFlag = FLAGS.get(challengeNumber);
        final AttackResult attackResult;
        if (expectedFlag.equals(flag)) {
            userTracker.assignmentSolved(webSession.getCurrentLesson(), "Assignment" + challengeNumber);
            attackResult = success(this).feedback("challenge.flag.correct").build();
        } else {
            userTracker.assignmentFailed(webSession.getCurrentLesson());
            attackResult = failed(this).feedback("challenge.flag.incorrect").build();
        }
        userTrackerRepository.save(userTracker);
        return attackResult;
    }
}
