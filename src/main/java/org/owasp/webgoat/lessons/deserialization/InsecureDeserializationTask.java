/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass; // Added for resolveClass
import java.io.InputStream; // Added for SafeObjectInputStream constructor
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
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
  public AttackResult completed(@RequestParam String token) throws IOException {
    String b64token;
    long before;
    long after;
    int delay;

    b64token = token.replace('-', '+').replace('_', '/');

    // Remediation: Using a custom ObjectInputStream to restrict deserializable classes (CWE-502)
    try (ObjectInputStream ois =
        new SafeObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(b64token)))) {
      before = System.currentTimeMillis();
      Object o = ois.readObject();
      if (!(o instanceof VulnerableTaskHolder)) {
        if (o instanceof String) {
          return failed(this).feedback("insecure-deserialization.stringobject").build();
        }
        return failed(this).feedback("insecure-deserialization.wrongobject").build();
      }
      after = System.currentTimeMillis();
    } catch (InvalidClassException e) {
      // This catch block will now also handle unauthorized class deserialization attempts
      return failed(this).feedback("insecure-deserialization.invalidversion").build();
    } catch (IllegalArgumentException e) {
      return failed(this).feedback("insecure-deserialization.expired").build();
    } catch (Exception e) {
      return failed(this).feedback("insecure-deserialization.invalidversion").build();
    }

    delay = (int) (after - before);
    if (delay > 7000) {
      return failed(this).build();
    }
    if (delay < 3000) {
      return failed(this).build();
    }
    return success(this).build();
  }

  /**
   * Custom ObjectInputStream that restricts deserialization to a whitelist of allowed classes.
   * This mitigates Insecure Deserialization (CWE-502) by preventing the deserialization
   * of arbitrary, potentially malicious, objects.
   */
  private static class SafeObjectInputStream extends ObjectInputStream {
      public SafeObjectInputStream(InputStream in) throws IOException {
          super(in);
      }

      @Override
      protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
          // Whitelist of allowed classes for deserialization.
          // VulnerableTaskHolder is the expected object for this lesson.
          // String is allowed because the original code explicitly checks for 'o instanceof String'.
          if (desc.getName().equals(VulnerableTaskHolder.class.getName()) ||
              desc.getName().equals(String.class.getName())) {
              return super.resolveClass(desc);
          }
          // For any other class, throw an InvalidClassException to prevent deserialization.
          throw new InvalidClassException("Unauthorized deserialization attempt", desc.getName());
      }
  }
}
