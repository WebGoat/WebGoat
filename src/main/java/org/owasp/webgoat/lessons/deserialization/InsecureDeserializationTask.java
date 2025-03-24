/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "insecure-deserialization.hints.1",
  "insecure-deserialization.hints.2",
  "insecure-deserialization.hints.3"
})
public class InsecureDeserializationTask implements AssignmentEndpoint {

  @PostMapping("/InsecureDeserialization/task")
  @ResponseBody
  public AttackResult completed(@RequestParam String token) {
    String b64token;
    long before;
    long after;
    int delay;

    // Заменяем символы для корректного декодирования Base64
    b64token = token.replace('-', '+').replace('_', '/');

    try {
      // Декодируем Base64 в строку
      String decodedToken = new String(Base64.getDecoder().decode(b64token), StandardCharsets.UTF_8);

      // Предположим, что токен имеет формат "taskName:delay" (например, "myTask:5000")
      String[] parts = decodedToken.split(":");
      if (parts.length != 2) {
        return failed(this).feedback("insecure-deserialization.wrongformat").build();
      }

      String taskName = parts[0];
      int taskDelay;

      // Проверяем корректность данных
      try {
        taskDelay = Integer.parseInt(parts[1]);
      } catch (NumberFormatException e) {
        return failed(this).feedback("insecure-deserialization.invalidnumber").build();
      }

      // Проверяем имя задачи (аналогично VulnerableTaskHolder)
      if (!"vulnerableTask".equals(taskName)) { // Здесь можно указать ожидаемое имя
        return failed(this).feedback("insecure-deserialization.wrongobject").build();
      }

      // Имитация задержки
      before = System.currentTimeMillis();
      Thread.sleep(taskDelay); // Используем контролируемую задержку
      after = System.currentTimeMillis();

      delay = (int) (after - before);
      if (delay > 7000) {
        return failed(this).build();
      }
      if (delay < 3000) {
        return failed(this).build();
      }
      return success(this).build();

    } catch (IllegalArgumentException e) {
      return failed(this).feedback("insecure-deserialization.invalidtoken").build();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return failed(this).feedback("insecure-deserialization.interrupted").build();
    } catch (Exception e) {
      return failed(this).feedback("insecure-deserialization.invalidversion").build();
    }
  }
}