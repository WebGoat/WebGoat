/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.mailbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Email implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore private LocalDateTime time = LocalDateTime.now();

  @Column(length = 1024)
  private String contents;

  private String sender;
  private String title;
  private String recipient;

  // Tracks whether the recipient has opened the mailbox since this mail arrived. "read" is a
  // reserved word in some databases, so the column is named read_flag. Used to show the number of
  // unread messages on the mailbox button. Server-controlled, so it is ignored on the incoming JSON
  // (a lesson delivering mail never sets it).
  @JsonIgnore
  @Column(name = "read_flag")
  private boolean read;

  public String getSummary() {
    return "-" + this.contents.substring(0, Math.min(50, contents.length()));
  }

  public LocalDateTime getTimestamp() {
    return time;
  }

  public String getTime() {
    return DateTimeFormatter.ofPattern("h:mm a").format(time);
  }

  public String getShortSender() {
    return sender.substring(0, sender.indexOf("@"));
  }
}
