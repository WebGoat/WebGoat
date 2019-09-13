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

package org.owasp.webwolf.mailbox;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webwolf.user.UserRepository;
import org.owasp.webwolf.user.WebGoatUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.Callable;

@RestController
@AllArgsConstructor
@Slf4j
public class MailboxController {

    private final MailboxRepository mailboxRepository;

    @GetMapping(value = "/WebWolf/mail")
    public ModelAndView mail() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ModelAndView modelAndView = new ModelAndView();
        List<Email> emails = mailboxRepository.findByRecipientOrderByTimeDesc(user.getUsername());
        if (emails != null && !emails.isEmpty()) {
            modelAndView.addObject("total", emails.size());
            modelAndView.addObject("emails", emails);
        }
        modelAndView.setViewName("mailbox");
        return modelAndView;
    }

    @PostMapping(value = "/mail")
    public Callable<ResponseEntity<?>> sendEmail(@RequestBody Email email) {
        return () -> {
            mailboxRepository.save(email);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        };
    }

}
