/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2017 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 */
package org.owasp.webgoat.assignments;

import lombok.Getter;
import org.owasp.webgoat.i18n.PluginMessages;
import org.owasp.webgoat.session.UserSessionData;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Each lesson can define an endpoint which can support the lesson. So for example if you create a lesson which uses JavaScript and
 * needs to call out to the server to fetch data you can define an endpoint in that lesson. WebGoat will pick up this endpoint and
 * Spring will publish it.
 * </p>
 * Extend this class and implement the met
 * </p>
 * Note: each subclass should declare this annotation otherwise the WebGoat framework cannot find your endpoint.
 */
public abstract class AssignmentEndpoint extends Endpoint {

    @Autowired
    private UserTrackerRepository userTrackerRepository;
    @Autowired
	private WebSession webSession;
    @Autowired
    private UserSessionData userSessionData;
    @Getter
    @Autowired
    private PluginMessages messages;

	//// TODO: 11/13/2016 events better fit?
    protected AttackResult trackProgress(AttackResult attackResult) {
        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        if (userTracker == null) {
            userTracker = new UserTracker(webSession.getUserName());
        }
        if (attackResult.assignmentSolved()) {
            userTracker.assignmentSolved(webSession.getCurrentLesson(), this.getClass().getSimpleName());
        } else {
            userTracker.assignmentFailed(webSession.getCurrentLesson());
        }
        userTrackerRepository.save(userTracker);
        return attackResult;
    }
    
    protected WebSession getWebSession() {
  		return webSession;
  	}

  	protected UserSessionData getUserSessionData() {
        return userSessionData;
    }

    @Override
    public final String getPath() {
        return this.getClass().getAnnotationsByType(AssignmentPath.class)[0].value();
    }

    /**
     * Convenience method for create a successful result:
     *
     * - Assignment is set to solved
     * - Feedback message is set to 'assignment.solved'
     *
     * Of course you can overwrite these values in a specific lesson
     *
     * @return a builder for creating a result from a lesson
     */
    protected AttackResult.AttackResultBuilder success() {
        return AttackResult.builder(messages).lessonCompleted(true).feedback("assignment.solved");
    }

    /**
     * Convenience method for create a failed result:
     *
     * - Assignment is set to not solved
     * - Feedback message is set to 'assignment.not.solved'
     *
     * Of course you can overwrite these values in a specific lesson
     *
     * @return a builder for creating a result from a lesson
     */
    protected AttackResult.AttackResultBuilder failed() {
        return AttackResult.builder(messages).lessonCompleted(false).feedback("assignment.not.solved");
    }

    protected AttackResult.AttackResultBuilder informationMessage() {
        return AttackResult.builder(messages).lessonCompleted(false);
    }
}
