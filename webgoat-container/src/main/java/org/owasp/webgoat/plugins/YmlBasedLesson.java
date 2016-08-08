package org.owasp.webgoat.plugins;

import org.owasp.webgoat.lessons.Attack;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.WebSession;

import java.util.List;

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
 * @since June 28, 2016
 */
public class YmlBasedLesson extends LessonAdapter {

    private final static Integer DEFAULT_RANKING = new Integer(10);
    private final String category;
    private final List<String> hints;
    private final String title;
    private final String id;
    private Attack attack;

    public YmlBasedLesson(String category, List<String> hints, String title, String id, Class attack) {
        this.category = category;
        this.hints = hints;
        this.title = title;
        this.id = id;
        createAttack(attack);

    }

    @Override
    protected Category getDefaultCategory() {
        return Category.getCategory(category);
    }

    @Override
    protected List<String> getHints(WebSession s) {
        return hints;
    }

    @Override
    protected Integer getDefaultRanking() {
        return DEFAULT_RANKING;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Attack getLessonAttack() {
        return this.attack;
    }

    private void createAttack(Class attack) {
        try {
            this.attack = (Attack) attack.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
