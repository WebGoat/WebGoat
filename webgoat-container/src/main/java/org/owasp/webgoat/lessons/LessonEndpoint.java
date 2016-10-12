/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
package org.owasp.webgoat.lessons;

import org.owasp.webgoat.lessons.model.AttackResult;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.UserTracker;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;

import java.io.File;

/**
 * Each lesson can define an endpoint which can support the lesson. So for example if you create a lesson which uses JavaScript and
 * needs to call out to the server to fetch data you can define an endpoint in that lesson. WebGoat will pick up this endpoint and
 * Spring will publish it.
 * </p>
 * Extend this class and implement the met
 * </p>
 * Note: each subclass should declare this annotation otherwise the WebGoat framework cannot find your endpoint.
 */
@LessonEndpointMapping
public abstract class LessonEndpoint implements MvcEndpoint {

    @Autowired
    @Qualifier("pluginTargetDirectory")
    private File pluginDirectory;
    @Autowired
    private WebSession webSession;
    private boolean solved = false;

    /**
     * The directory of the plugin directory in which the lessons resides, so if you want to access the lesson 'ClientSideFiltering' you will
     * need to:
     *
     * <code>
     *     File lessonDirectory = new File(getPluginDirectory(), "ClientSideFiltering");
     * </code>
     *
     * The directory structure of the lesson is exactly the same as the directory structure in the plugins project.
     *
     * @return the top level
     */
    protected File getPluginDirectory() {
        return new File(this.pluginDirectory, "plugin");
    }

    protected LessonTracker getLessonTracker() {
        UserTracker userTracker = UserTracker.instance();
        LessonTracker lessonTracker = userTracker.getLessonTracker(webSession, webSession.getCurrentLesson());
        return lessonTracker;
    }

    protected AttackResult trackProgress(AttackResult attackResult) {
        this.solved = attackResult.isLessonCompleted();
        getLessonTracker().setCompleted(solved);
        return attackResult;
    }

    @Override
    public final boolean isSensitive() {
        return false;
    }

    @Override
    public final Class<? extends Endpoint> getEndpointType() {
        return null;
    }

}
