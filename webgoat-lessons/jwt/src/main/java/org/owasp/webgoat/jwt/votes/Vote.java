/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.jwt.votes;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;

/**
 * @author nbaars
 * @since 5/2/17.
 */
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


    public Vote(String title, String information, String imageSmall, String imageBig, int numberOfVotes, int totalVotes) {
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