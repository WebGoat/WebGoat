package org.owasp.webgoat.plugin.introduction;

import java.util.ArrayList;
import java.util.List;

import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.NewLesson;

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
 * @since October 12, 2016
 */
public class SqlInjection extends NewLesson {
    @Override
    public Category getDefaultCategory() {
        return Category.INJECTION;
    }

    @Override
    public List<String> getHints() {
        List<String> hints = new ArrayList<String>();
        
//        hints.add(getLabelManager().get("SqlStringInjectionHint1"));
//        hints.add(getLabelManager().get("SqlStringInjectionHint2"));
//        hints.add(getLabelManager().get("SqlStringInjectionHint3"));
//        hints.add(getLabelManager().get("SqlStringInjectionHint4"));
//        hints.add(getLabelManager().get("SqlStringInjectionHint5"));
        return hints;
    }

    @Override
    public Integer getDefaultRanking() {
        return 0;
    }

    @Override
    public String getTitle() {
        return "sql.injection.title";
    }

    @Override
    public String getId() {
        return "SqlInjection";
    }
}
