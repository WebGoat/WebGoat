/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class HashingAssignmentTest {

  private HashingAssignment hashingAssignment;
  private HttpServletRequest request;
  private HttpSession session;

  @BeforeEach
  void setup() {
    hashingAssignment = new HashingAssignment();
    request = mock(HttpServletRequest.class);
    session = mock(HttpSession.class);
    when(request.getSession()).thenReturn(session);
  }

  @Test
  void shouldGenerateDeterministicMd5ForSession() throws NoSuchAlgorithmException {
    when(session.getAttribute("md5Hash")).thenReturn(null);

    String hash1 = hashingAssignment.getMd5(request);

    when(session.getAttribute("md5Hash")).thenReturn(hash1);

    String hash2 = hashingAssignment.getMd5(request);

    assertThat(hash1).isEqualTo(hash2);
  }

  @Test
  void shouldGenerateDeterministicSha256ForSession() throws NoSuchAlgorithmException {
    when(session.getAttribute("sha256")).thenReturn(null);

    String hash1 = hashingAssignment.getSha256(request);

    when(session.getAttribute("sha256")).thenReturn(hash1);

    String hash2 = hashingAssignment.getSha256(request);

    assertThat(hash1).isEqualTo(hash2);
  }

  @Test
  void shouldReturnSuccessWhenBothSecretsAreCorrect() {
    when(session.getAttribute("md5Secret")).thenReturn("secret1");
    when(session.getAttribute("sha256Secret")).thenReturn("secret2");

    AttackResult result = hashingAssignment.completed(request, "secret1", "secret2");

    assertThat(result.getLessonCompleted()).isTrue();
  }

  @Test
  void shouldReturnPartialSuccessWhenOneSecretIsCorrect() {
    when(session.getAttribute("md5Secret")).thenReturn("secret1");
    when(session.getAttribute("sha256Secret")).thenReturn("secret2");

    AttackResult result = hashingAssignment.completed(request, "secret1", "wrong");

    assertThat(result.getLessonCompleted()).isFalse();
  }

  @Test
  void shouldReturnFailureWhenSecretsAreIncorrectOrMissing() {
    when(session.getAttribute("md5Secret")).thenReturn("secret1");
    when(session.getAttribute("sha256Secret")).thenReturn("secret2");

    AttackResult result1 = hashingAssignment.completed(request, "wrong1", "wrong2");
    AttackResult result2 = hashingAssignment.completed(request, null, null);

    assertThat(result1.getLessonCompleted()).isFalse();
    assertThat(result2.getLessonCompleted()).isFalse();
  }
}
