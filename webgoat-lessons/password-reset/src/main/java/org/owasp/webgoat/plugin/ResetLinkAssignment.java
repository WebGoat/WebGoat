package org.owasp.webgoat.plugin;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.resetlink.PasswordChangeForm;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@AssignmentPath("/PasswordReset/reset")
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


    @PostMapping("/login")
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

    @GetMapping("/reset-password/{link}")
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

    @PostMapping("/change-password")
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
