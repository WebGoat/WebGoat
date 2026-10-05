/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.vulnerablecomponents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.ForbiddenClassException;
import org.junit.jupiter.api.Test;

class VulnerableComponentsLessonTest {

  private static final String CONTACT_XML =
      "<contact><firstName>WebGoat</firstName></contact>";

  @Test
  void shouldDeserializeAllowlistedContact() {
    XStream xstream = VulnerableComponentsLesson.createSecureXStream();

    Object result = xstream.fromXML(CONTACT_XML);

    assertThat(result).isInstanceOf(ContactImpl.class);
    assertThat(((ContactImpl) result).getFirstName()).isEqualTo("WebGoat");
  }

  @Test
  void shouldRejectTypesOutsideAllowlist() {
    XStream xstream = VulnerableComponentsLesson.createSecureXStream();
    String xml = "<contact class='java.util.ArrayList'/>";

    assertThrows(ForbiddenClassException.class, () -> xstream.fromXML(xml));
  }
}
