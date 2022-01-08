package org.owasp.webgoat.container.session;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;

import java.util.List;

/**
 * ************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2014 Bruce Mayhew
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
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @version $Id: $Id
 * @since October 28, 2003
 */
@Slf4j
public class Course {

    private List<Lesson> lessons;

    public Course(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    /**
     * Gets the categories attribute of the Course object
     *
     * @return The categories value
     */
    public List<Category> getCategories() {
        return lessons.parallelStream().map(Lesson::getCategory).distinct().sorted().toList();
    }

    /**
     * Gets the firstLesson attribute of the Course object
     *
     * @return The firstLesson value
     */
    public Lesson getFirstLesson() {
        // Category 0 is the admin function. We want the first real category
        // to be returned. This is normally the General category and the Http Basics lesson
        return getLessons(getCategories().get(0)).get(0);
    }

    /**
     * <p>Getter for the field <code>lessons</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Lesson> getLessons() {
        return this.lessons;
    }

    /**
     * <p>Getter for the field <code>lessons</code>.</p>
     *
     * @param category a {@link org.owasp.webgoat.container.lessons.Category} object.
     * @return a {@link java.util.List} object.
     */
    public List<Lesson> getLessons(Category category) {
        return this.lessons.stream().filter(l -> l.getCategory() == category).toList();
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public int getTotalOfLessons() {
        return this.lessons.size();
    }

    public int getTotalOfAssignments() {
        return this.lessons.stream().reduce(0, (total, lesson) -> lesson.getAssignments().size() + total, Integer::sum);
    }
}
