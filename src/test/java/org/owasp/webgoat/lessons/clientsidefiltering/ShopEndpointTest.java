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

package org.owasp.webgoat.lessons.clientsidefiltering;

import static org.hamcrest.Matchers.is;
import static org.owasp.webgoat.lessons.clientsidefiltering.ClientSideFilteringFreeAssignment.SUPER_COUPON_CODE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author nbaars
 * @since 5/2/17.
 */
public class ShopEndpointTest extends LessonTest {

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = standaloneSetup(new ShopEndpoint()).build();
  }

  @Test
  public void getSuperCoupon() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                "/clientSideFiltering/challenge-store/coupons/" + SUPER_COUPON_CODE))
        .andExpect(jsonPath("$.code", CoreMatchers.is(SUPER_COUPON_CODE)))
        .andExpect(jsonPath("$.discount", CoreMatchers.is(100)));
  }

  @Test
  public void getCoupon() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/clientSideFiltering/challenge-store/coupons/webgoat"))
        .andExpect(jsonPath("$.code", CoreMatchers.is("webgoat")))
        .andExpect(jsonPath("$.discount", CoreMatchers.is(25)));
  }

  @Test
  public void askForUnknownCouponCode() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                "/clientSideFiltering/challenge-store/coupons/does-not-exists"))
        .andExpect(jsonPath("$.code", CoreMatchers.is("no")))
        .andExpect(jsonPath("$.discount", CoreMatchers.is(0)));
  }

  @Test
  public void fetchAllTheCouponsShouldContainGetItForFree() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/clientSideFiltering/challenge-store/coupons/"))
        .andExpect(jsonPath("$.codes[3].code", is("get_it_for_free")));
  }
}
