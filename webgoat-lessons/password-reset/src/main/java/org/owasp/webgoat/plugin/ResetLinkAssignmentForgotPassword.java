package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Part of the password reset assignment. Used to send the e-mail.
 * @author nbaars
 * @since 8/20/17.
 */
@AssignmentPath("/PasswordReset/ForgotPassword")
public class ResetLinkAssignmentForgotPassword extends AssignmentEndpoint {

  private final RestTemplate restTemplate;
  private final String webWolfMailURL;

  public ResetLinkAssignmentForgotPassword(RestTemplate restTemplate,
                                           @Value("${webwolf.url.mail}") String webWolfMailURL) {
    this.restTemplate = restTemplate;
    this.webWolfMailURL = webWolfMailURL;
  }

  @RequestMapping(method = POST, value = "/create-password-reset-link")
  @ResponseBody
  public AttackResult sendPasswordResetLink(@RequestParam String email, HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
    String resetLink = UUID.randomUUID().toString();
    ResetLinkAssignment.resetLinks.add(resetLink);
    String host = request.getHeader("host");
    if (org.springframework.util.StringUtils.hasText(email)) {
      if (email.equals(ResetLinkAssignment.TOM_EMAIL) && host.contains("9090")) { //User indeed changed the host header.
        ResetLinkAssignment.userToTomResetLink.put(getWebSession().getUserName(), resetLink);
        fakeClickingLinkEmail(host, resetLink);
      } else {
        try {
          sendMailToUser(email, host, resetLink);
        } catch(Exception e) { return failed().output("E-mail can't be send. please try again.").build(); }
      }
    }
    return success().feedback("email.send").feedbackArgs(email).build();
  }

  private void sendMailToUser(@RequestParam String email, String host, String resetLink) {
    int index = email.indexOf("@");
    String username = email.substring(0, index == -1 ? email.length() : index);
    PasswordResetEmail mail = PasswordResetEmail.builder()
            .title("Your password reset link")
            .contents(String.format(ResetLinkAssignment.TEMPLATE, host, resetLink))
            .sender("password-reset@webgoat-cloud.net")
            .recipient(username)
            .time(LocalDateTime.now()).build();
    this.restTemplate.postForEntity(webWolfMailURL, mail, Object.class);
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

}
