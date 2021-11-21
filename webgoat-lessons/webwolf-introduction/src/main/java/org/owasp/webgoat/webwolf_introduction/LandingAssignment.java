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

package org.owasp.webgoat.webwolf_introduction;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
public class LandingAssignment extends AssignmentEndpoint {

    @Value("${webwolf.landingpage.url}")
    private String landingPageUrl;

    @PostMapping("/WebWolf/landing")
    @ResponseBody
    public AttackResult click(String uniqueCode) {
        if (StringUtils.reverse(getWebSession().getUserName()).equals(uniqueCode)) {
            return success(this).build();
        }
        return failed(this).feedback("webwolf.landing_wrong").build();
    }


    @GetMapping("/WebWolf/landing/password-reset")
    public ModelAndView openPasswordReset(HttpServletRequest request) throws URISyntaxException {
        URI uri = new URI(request.getRequestURL().toString());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("webwolfUrl", landingPageUrl);
        modelAndView.addObject("uniqueCode", StringUtils.reverse(getWebSession().getUserName()));

        modelAndView.setViewName("webwolfPasswordReset");
        return modelAndView;
    }
}
