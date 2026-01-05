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
import java.io.ObjectStreamClass; // New import for ObjectStreamClass
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

  /**
   * Custom ObjectInputStream that whitelists allowed classes during deserialization.
   * This prevents deserialization of arbitrary types, mitigating gadget chain attacks.
   */
  private static class WhitelistingObjectInputStream extends ObjectInputStream {
      private static final String ALLOWED_APPLICATION_CLASS = "org.dummy.insecure.framework.VulnerableTaskHolder";

      public WhitelistingObjectInputStream(ByteArrayInputStream in) throws IOException {
          super(in);
      }

      @Override
      protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
          String className = desc.getName();

          // Explicitly allow the expected application class
          if (className.equals(ALLOWED_APPLICATION_CLASS)) {
              return super.resolveClass(desc);
          }

          // Allow standard JDK classes (java.* and javax.*)
          // This is a common and generally safe practice for deserialization whitelisting
          // as it prevents gadget chains from non-JDK libraries while allowing basic types.
          if (className.startsWith("java.") || className.startsWith("javax.")) {
              return super.resolveClass(desc);
          }

          // If the class is not explicitly allowed, throw an InvalidClassException
          // to prevent deserialization of unauthorized types.
          throw new InvalidClassException("Unauthorized deserialization attempt: " + className);
      }
  }

  @PostMapping("/InsecureDeserialization/task")
  @ResponseBody
  public AttackResult completed(@RequestParam String token) { // Removed 'throws IOException' as all checked exceptions are now caught
    String b64token;
    long before;
    long after;
    int delay;

    b64token = token.replace('-', '+').replace('_', '/');

    try (ObjectInputStream ois =
        new WhitelistingObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(b64token)))) {
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
      // Catches InvalidClassException from our resolveClass or standard deserialization issues
      return failed(this).feedback("insecure-deserialization.invalidclass").build();
    } catch (ClassNotFoundException e) {
      // Catches if a whitelisted class (or any class) cannot be found during deserialization
      return failed(this).feedback("insecure-deserialization.classnotfound").build();
    } catch (IOException e) {
      // Catches general IO issues during stream processing or malformed data within the stream
      return failed(this).feedback("insecure-deserialization.ioerror").build();
    } catch (IllegalArgumentException e) {
      // Catches issues with Base64 decoding (e.g., malformed Base64 string)
      return failed(this).feedback("insecure-deserialization.malformedbase64").build();
    } catch (Exception e) {
      // Catch any other unexpected exceptions during the process
      return failed(this).feedback("insecure-deserialization.unexpectederror").build();
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
}
