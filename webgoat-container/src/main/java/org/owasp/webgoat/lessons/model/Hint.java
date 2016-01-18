/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 */
package org.owasp.webgoat.lessons.model;

/**
 * <p>Hint class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
public class Hint {

    private String hint;
    private String lesson;
    private int number;

    /**
     * <p>Getter for the field <code>hint</code>.</p>
     *
     * @return the hint
     */
    public String getHint() {
        return hint;
    }

    /**
     * <p>Setter for the field <code>hint</code>.</p>
     *
     * @param hint the hint to set
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * <p>Getter for the field <code>lesson</code>.</p>
     *
     * @return the lesson
     */
    public String getLesson() {
        return lesson;
    }

    /**
     * <p>Setter for the field <code>lesson</code>.</p>
     *
     * @param lesson the lesson to set
     */
    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    /**
     * <p>Getter for the field <code>number</code>.</p>
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * <p>Setter for the field <code>number</code>.</p>
     *
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

}
