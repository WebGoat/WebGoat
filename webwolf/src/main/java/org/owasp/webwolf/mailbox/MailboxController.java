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

/**
 * @author nbaars
 * @since 8/17/17.
 */
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
