package org.owasp.webgoat.lessons;

import lombok.Getter;

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
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since August 13, 2016
 */
@Getter
public class AttackResult {

    private boolean lessonCompleted;
    private String feedback;
    private String output;

    public static AttackResult success() {
        return AttackResult.success("Congratulations");
    }

    public static AttackResult success(String feedback) {
        return success(feedback, "");
    }

    public static AttackResult success(String feedback, String output) {
        AttackResult attackResult = new AttackResult();
        attackResult.lessonCompleted = true;
        attackResult.feedback = feedback;
        attackResult.output = output;
        return attackResult;
    }

    public static AttackResult failed(String feedback) {
        return failed(feedback, "");
    }

    public static AttackResult failed(String feedback, String output) {
        AttackResult attackResult = new AttackResult();
        attackResult.lessonCompleted = false;
        attackResult.feedback = feedback;
        attackResult.output = output;
        return attackResult;
    }

    public boolean assignmentSolved() {
        return lessonCompleted;
    }
}
