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

package org.owasp.webgoat.webwolf.mailbox;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class MailboxController {

  private final MailboxRepository mailboxRepository;

  @GetMapping("/mail")
  public ModelAndView mail(Authentication authentication, Model model) {
    String username = (null != authentication) ? authentication.getName() : "anonymous";
    ModelAndView modelAndView = new ModelAndView();
    List<Email> emails = mailboxRepository.findByRecipientOrderByTimeDesc(username);
    if (emails != null && !emails.isEmpty()) {
      modelAndView.addObject("total", emails.size());
      modelAndView.addObject("emails", emails);
    }
    modelAndView.setViewName("mailbox");
    model.addAttribute("username", username);
    return modelAndView;
  }

  @PostMapping("/mail")
  @ResponseStatus(HttpStatus.CREATED)
  public void sendEmail(@RequestBody Email email) {
    mailboxRepository.save(email);
  }

  @DeleteMapping("/mail")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void deleteAllMail() {
    mailboxRepository.deleteAll();
  }
}
