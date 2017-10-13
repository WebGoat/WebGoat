package org.owasp.webwolf.mailbox;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webwolf.user.UserRepository;
import org.owasp.webwolf.user.WebGoatUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author nbaars
 * @since 8/17/17.
 */
@RestController
@AllArgsConstructor
@Slf4j
public class MailboxController {

    private final UserRepository userRepository;
    private final MailboxRepository mailboxRepository;

    @GetMapping(value = "/WebWolf/mail")
    public ModelAndView mail() {
        WebGoatUser user = (WebGoatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    @ResponseStatus(HttpStatus.CREATED)
    public void sendEmail(@RequestBody Email email) {
        if (userRepository.findByUsername(email.getRecipient()) != null) {
            mailboxRepository.save(email);
        } else {
            log.trace("Mail received for unknown user: {}", email.getRecipient());
        }
    }

}
