package org.owasp.webgoat.users;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * @author nbaars
 * @since 3/19/17.
 */
@Controller
@AllArgsConstructor
@Slf4j
public class RegistrationController {

    private UserValidator userValidator;
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @GetMapping("/registration")
    public String showForm(UserForm userForm) {
        return "registration";
    }

    @PostMapping("/register.mvc")
    public String registration(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.addUser(userForm.getUsername(), userForm.getPassword());
        autologin(userForm.getUsername(), userForm.getPassword());

        return "redirect:/attack";
    }

    private void autologin(String username, String password) {
        WebGoatUser user = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            log.debug("Login for {} successfully!", username);
        }
    }


}
