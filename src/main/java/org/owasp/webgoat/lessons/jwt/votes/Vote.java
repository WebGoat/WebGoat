/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.jwt.votes;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;

@Getter
public class Vote {
  @JsonView(Views.GuestView.class)
  private final String title;

  @JsonView(Views.GuestView.class)
  private final String information;

  @JsonView(Views.GuestView.class)
  private final String imageSmall;

  @JsonView(Views.GuestView.class)
  private final String imageBig;

  @JsonView(Views.UserView.class)
  private int numberOfVotes;

  @JsonView(Views.UserView.class)
  private boolean votingAllowed = true;

  @JsonView(Views.UserView.class)
  private long average = 0;

  public Vote(
      String title,
      String information,
      String imageSmall,
      String imageBig,
      int numberOfVotes,
      int totalVotes) {
    this.title = title;
    this.information = information;
    this.imageSmall = imageSmall;
    this.imageBig = imageBig;
    this.numberOfVotes = numberOfVotes;
    this.average = calculateStars(totalVotes);
  }

  public void incrementNumberOfVotes(int totalVotes) {
    this.numberOfVotes = this.numberOfVotes + 1;
    this.average = calculateStars(totalVotes);
  }

  public void reset() {
    this.numberOfVotes = 1;
    this.average = 1;
  }

  private long calculateStars(int totalVotes) {
    return Math.round(((double) numberOfVotes / (double) totalVotes) * 4);
  }
}
