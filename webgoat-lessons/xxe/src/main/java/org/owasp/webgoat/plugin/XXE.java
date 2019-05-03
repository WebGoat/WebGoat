package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.NewLesson;

import java.util.ArrayList;
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
 * @author nbaars
 * @version $Id: $Id
 * @since November 17, 2016
 */
public class XXE extends NewLesson {

    @Override
    public Category getDefaultCategory() {
        return Category.INJECTION;
    }

    @Override
    public List<String> getHints() {
        List<String> hints = new ArrayList<String>();
        hints.add("Try submitting the form and see what happens");
        hints.add("XXE stands for XML External Entity attack");
        hints.add("Try to include your own DTD");
        return hints;
    }

    @Override
    public Integer getDefaultRanking() {
        return 4;
    }

    @Override
    public String getTitle() {
        return "xxe.title";
    }

    @Override
    public String getId() {
        return "XXE";
    }
}
