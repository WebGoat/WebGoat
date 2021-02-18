/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at
 * https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>LessonMenuItem class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
public class LessonMenuItem {

    private String name;
    private LessonMenuItemType type;
    private List<LessonMenuItem> children = new ArrayList<LessonMenuItem>();
    private boolean complete;
    private String link;
    private int ranking;

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>children</code>.</p>
     *
     * @return the children
     */
    public List<LessonMenuItem> getChildren() {
        return children;
    }

    /**
     * <p>Setter for the field <code>children</code>.</p>
     *
     * @param children the children to set
     */
    public void setChildren(List<LessonMenuItem> children) {
        this.children = children;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return the type
     */
    public LessonMenuItemType getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type the type to set
     */
    public void setType(LessonMenuItemType type) {
        this.type = type;
    }

    /**
     * <p>addChild.</p>
     *
     * @param child a {@link LessonMenuItem} object.
     */
    public void addChild(LessonMenuItem child) {
        children.add(child);
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("Name: ").append(name).append(" | ");
        bldr.append("Type: ").append(type).append(" | ");
        return bldr.toString();
    }

    /**
     * <p>isComplete.</p>
     *
     * @return the complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * <p>Setter for the field <code>complete</code>.</p>
     *
     * @param complete the complete to set
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * <p>Getter for the field <code>link</code>.</p>
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * <p>Setter for the field <code>link</code>.</p>
     *
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getRanking() {
        return this.ranking;
    }


}
