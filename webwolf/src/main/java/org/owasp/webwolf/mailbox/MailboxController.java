package org.owasp.webwolf.mailbox;

import lombok.AllArgsConstructor;
import org.owasp.webwolf.user.WebGoatUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author nbaars
 * @since 8/17/17.
 */
@RestController
@AllArgsConstructor
public class MailboxController {

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

}
