/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.dummy.insecure.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import java.io.ObjectInputFilter; // I added 

@Slf4j
// TODO move back to lesson
public class VulnerableTaskHolder implements Serializable {

  private static final long serialVersionUID = 2;

  private String taskName;
  private String taskAction;
  private LocalDateTime requestedExecutionTime;

  public VulnerableTaskHolder(String taskName, String taskAction) {
    super();
    this.taskName = taskName;
    this.taskAction = taskAction;
    this.requestedExecutionTime = LocalDateTime.now();
  }

  @Override
  public String toString() {
    return "VulnerableTaskHolder [taskName="
        + taskName
        + ", taskAction="
        + taskAction
        + ", requestedExecutionTime="
        + requestedExecutionTime
        + "]";
  }

  /**
   * Safely de-serialize a saved or received object using ObjectInputFilter
   * and removing dynamic OS command execution.
   */
  private void readObject(ObjectInputStream stream) throws Exception {
    // 1\. Layer 1 Mitigation: Apply JVM Class Filter (Allowlist safe classes, reject all others with ';!\*')
    ObjectInputFilter filter = ObjectInputFilter.Config.createFilter( "java.time.LocalDateTime;java.lang.String;org.dummy.insecure.framework.SecureTaskHolder;!\*" );
    stream.setObjectInputFilter(filter);
    // 2\. Safely restore serialized fields (throws InvalidClassException if object stream contains unauthorized classes) 
    stream.defaultReadObject();;

    // do something with the data
    log.info("restoring task: {}", taskName);
    log.info("restoring time: {}", requestedExecutionTime);

    if (requestedExecutionTime != null
        && (requestedExecutionTime.isBefore(LocalDateTime.now().minusMinutes(10))
            || requestedExecutionTime.isAfter(LocalDateTime.now()))) {
      // do nothing is the time is not within 10 minutes after the object has been created
      log.debug(this.toString());
      throw new IllegalArgumentException("outdated");
    }

    // 3\. Layer 1 Mitigation (Sink Removal): Removed Runtime.getRuntime().exec() entirely. 
    // Untrusted strings restored from a stream are never passed directly to OS execution sinks.
    // condition is here to prevent you from destroying the goat altogether
    log.info("Task '{}' validated and queued safely without process execution.", taskName);
    /**if ((taskAction.startsWith("sleep") || taskAction.startsWith("ping"))
        && taskAction.length() < 22) {
      log.info("about to execute: {}", taskAction);
      try {
        Process p = Runtime.getRuntime().exec(taskAction);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while ((line = in.readLine()) != null) {
          log.info(line);
        }
      } catch (IOException e) {
        log.error("IO Exception", e);
        */
      }
    }
  }
}
