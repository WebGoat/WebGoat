package org.owasp.webgoat.plugin;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.PasswordResetEmail;
import org.owasp.webgoat.plugin.resetlink.PasswordChangeForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@AssignmentPath("/PasswordReset/reset")
@AssignmentHints({"password-reset-hint1", "password-reset-hint2", "password-reset-hint3", "password-reset-hint4", "password-reset-hint5"})
public class ResetLinkAssignment extends AssignmentEndpoint {

    private static final String PASSWORD_TOM_9 = "somethingVeryRandomWhichNoOneWillEverTypeInAsPasswordForTom";
    private static final String TOM_EMAIL = "tom@webgoat-cloud.org";
    private static Map<String, String> userToTomResetLink = Maps.newHashMap();
    private static Map<String, String> usersToTomPassword = Maps.newHashMap();
    private static EvictingQueue resetLinks = EvictingQueue.create(1000);

    private static final String TEMPLATE = "Hi, you requested a password reset link, please use this " +
            "<a target='_blank' href='http://%s/WebGoat/PasswordReset/reset/reset-password/%s'>link</a> to reset your password." +
            "\n \n\n" +
            "If you did not request this password change you can ignore this message." +
            "\n" +
            "If you have any comments or questions, please do not hesitate to reach us at support@webgoat-cloud.org" +
            "\n\n" +
            "Kind regards, \nTeam WebGoat";

    private final RestTemplate restTemplate;
    private final String webWolfMailURL;

    public ResetLinkAssignment(RestTemplate restTemplate, @Value("${webwolf.url.mail}") String webWolfMailURL) {
        this.restTemplate = restTemplate;
        this.webWolfMailURL = webWolfMailURL;
    }

    @RequestMapping(method = POST, value = "/create-password-reset-link")
    @ResponseBody
    public AttackResult sendPasswordResetLink(@RequestParam String email, HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
        String resetLink = UUID.randomUUID().toString();
        resetLinks.add(resetLink);
        String host = request.getHeader("host");
        if (org.springframework.util.StringUtils.hasText(email)) {
            if (email.equals(TOM_EMAIL) && host.contains("9090")) { //User indeed changed the host header.
                userToTomResetLink.put(getWebSession().getUserName(), resetLink);
                fakeClickingLinkEmail(host, resetLink);
            } else {
                sendMailToUser(email, host, resetLink);
            }
        }
        return success().feedback("email.send").feedbackArgs(email).build();
    }

    private void sendMailToUser(@RequestParam String email, String host, String resetLink) {
        int index = email.indexOf("@");
        String username = email.substring(0, index == -1 ? email.length() : index);
        PasswordResetEmail mail = PasswordResetEmail.builder()
                .title("Your password reset link")
                .contents(String.format(TEMPLATE, host, resetLink))
                .sender("password-reset@webgoat-cloud.net")
                .recipient(username)
                .time(LocalDateTime.now()).build();
        restTemplate.postForEntity(webWolfMailURL, mail, Object.class);
    }

    /**
     * We need to add the current cookie of the user otherwise we cannot distinguish in WebWolf for
     * which user we need to trace the incoming request. In normal situation this HOST will be in your
     * full control so every incoming request would be valid.
     */
    private void fakeClickingLinkEmail(String host, String resetLink) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            new RestTemplate().exchange(String.format("http://%s/PasswordReset/reset/reset-password/%s", host, resetLink), HttpMethod.GET, httpEntity, Void.class);
        } catch (Exception e) {
            //don't care
        }
    }

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
