package org.owasp.webgoat.plugin.challenge7;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.Email;
import org.owasp.webgoat.plugin.SolutionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static org.owasp.webgoat.plugin.Flag.FLAGS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@AssignmentPath("/challenge/7")
@Slf4j
public class Assignment7 extends AssignmentEndpoint {

    private static final String TEMPLATE = "Hi, you requested a password reset link, please use this " +
            "<a target='_blank' href='%s:8080/WebGoat/challenge/7/reset-password/%s'>link</a> to reset your password." +
            "\n \n\n" +
            "If you did not request this password change you can ignore this message." +
            "\n" +
            "If you have any comments or questions, please do not hesitate to reach us at support@webgoat-cloud.org" +
            "\n\n" +
            "Kind regards, \nTeam WebGoat";

    @Autowired
    private RestTemplate restTemplate;
    @Value("${webwolf.url.mail}")
    private String webWolfMailURL;

    @GetMapping("/reset-password/{link}")
    public ResponseEntity<String> resetPassword(@PathVariable(value = "link") String link) {
        if (link.equals(SolutionConstants.ADMIN_PASSWORD_LINK)) {
            return ResponseEntity.accepted().body("<h1>Success!!</h1>" +
                    "<img src='/WebGoat/images/hi-five-cat.jpg'>" +
                    "<br/><br/>Here is your flag: " + "<b>" + FLAGS.get(7) + "</b>");
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("That is not the reset link for admin");
    }

    @RequestMapping(method = POST)
    @ResponseBody
    public AttackResult sendPasswordResetLink(@RequestParam String email, HttpServletRequest request) throws URISyntaxException {
        if (StringUtils.hasText(email)) {
            String username = email.substring(0, email.indexOf("@"));
            if (StringUtils.hasText(username)) {
                URI uri = new URI(request.getRequestURL().toString());
                Email mail = Email.builder()
                        .title("Your password reset link for challenge 7")
                        .contents(String.format(TEMPLATE, uri.getScheme() + "://" + uri.getHost(), new PasswordResetLink().createPasswordReset(username, "webgoat")))
                        .sender("password-reset@webgoat-cloud.net")
                        .recipient(username)
                        .time(LocalDateTime.now()).build();
                restTemplate.postForEntity(webWolfMailURL, mail, Object.class);
            }
        }
        return success().feedback("email.send").feedbackArgs(email).build();
    }

    @RequestMapping(method = GET, value = "/.git", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    @SneakyThrows
    public ClassPathResource git() {
        return new ClassPathResource("challenge7/git.zip");
    }
}

