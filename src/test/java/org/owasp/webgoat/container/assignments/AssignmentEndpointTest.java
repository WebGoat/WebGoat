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

package org.owasp.webgoat.container.assignments;

import java.util.Locale;
import org.mockito.Mock;
import org.owasp.webgoat.WithWebGoatUser;
import org.owasp.webgoat.container.i18n.Language;
import org.owasp.webgoat.container.i18n.Messages;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.users.UserProgress;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

// Do not remove is the base class for all assignments tests

@WithWebGoatUser
public class AssignmentEndpointTest {

  @Mock protected UserProgress userTracker;
  @Mock protected UserProgressRepository userTrackerRepository;

  private Language language =
      new Language(new FixedLocaleResolver()) {
        @Override
        public Locale getLocale() {
          return Locale.ENGLISH;
        }
      };
  protected Messages messages = new Messages(language);
  protected PluginMessages pluginMessages =
      new PluginMessages(messages, language, new ClassPathXmlApplicationContext());

  public void init(AssignmentEndpoint a) {
    messages.setBasenames("classpath:/i18n/messages", "classpath:/i18n/WebGoatLabels");
    ReflectionTestUtils.setField(a, "messages", pluginMessages);
  }
}
