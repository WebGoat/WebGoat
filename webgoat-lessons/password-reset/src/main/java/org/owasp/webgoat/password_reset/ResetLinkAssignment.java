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

package org.owasp.webgoat.password_reset;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.password_reset.resetlink.PasswordChangeForm;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
@AssignmentHints({"password-reset-hint1", "password-reset-hint2", "password-reset-hint3", "password-reset-hint4", "password-reset-hint5", "password-reset-hint6"})
public class ResetLinkAssignment extends AssignmentEndpoint {

    static final String PASSWORD_TOM_9 = "somethingVeryRandomWhichNoOneWillEverTypeInAsPasswordForTom";
    static final String TOM_EMAIL = "tom@webgoat-cloud.org";
    static Map<String, String> userToTomResetLink = Maps.newHashMap();
    static Map<String, String> usersToTomPassword = Maps.newHashMap();
    static EvictingQueue resetLinks = EvictingQueue.create(1000);

    static final String TEMPLATE = "Hi, you requested a password reset link, please use this " +
            "<a target='_blank' href='http://%s/WebGoat/PasswordReset/reset/reset-password/%s'>link</a> to reset your password." +
            "\n \n\n" +
            "If you did not request this password change you can ignore this message." +
            "\n" +
            "If you have any comments or questions, please do not hesitate to reach us at support@webgoat-cloud.org" +
            "\n\n" +
            "Kind regards, \nTeam WebGoat";


    @PostMapping("/PasswordReset/reset/login")
    @ResponseBody
    public AttackResult login(@RequestParam String password, @RequestParam String email) {
        if (TOM_EMAIL.equals(email)) {
            String passwordTom = usersToTomPassword.getOrDefault(getWebSession().getUserName(), PASSWORD_TOM_9);
            if (passwordTom.equals(PASSWORD_TOM_9)) {
                return trackProgress(failed().feedback("login_failed").build());
            } else if (passwordTom.equals(password)) {
                return trackProgress(success().build());
            }
        }
        return trackProgress(failed().feedback("login_failed.tom").build());
    }

    @GetMapping("/PasswordReset/reset/reset-password/{link}")
    public String resetPassword(@PathVariable(value = "link") String link, Model model) {
        if (this.resetLinks.contains(link)) {
            PasswordChangeForm form = new PasswordChangeForm();
            form.setResetLink(link);
            model.addAttribute("form", form);
            return "password_reset"; //Display html page for changing password
        } else {
            return "password_link_not_found";
        }
    }

    @PostMapping("/PasswordReset/reset/change-password")
    public String changePassword(@ModelAttribute("form") PasswordChangeForm form, BindingResult bindingResult) {
        if (!org.springframework.util.StringUtils.hasText(form.getPassword())) {
            bindingResult.rejectValue("password", "not.empty");
        }
        if (bindingResult.hasErrors()) {
            return "password_reset";
        }
        if (!resetLinks.contains(form.getResetLink())) {
            return "password_link_not_found";
        }
        if (checkIfLinkIsFromTom(form.getResetLink())) {
            usersToTomPassword.put(getWebSession().getUserName(), form.getPassword());
        }
        return "success";
    }

    private boolean checkIfLinkIsFromTom(String resetLinkFromForm) {
        String resetLink = userToTomResetLink.getOrDefault(getWebSession().getUserName(), "unknown");
        return resetLink.equals(resetLinkFromForm);
    }
}
