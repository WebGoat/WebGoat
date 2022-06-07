package org.owasp.webgoat.container.users;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
    public String registration(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult, HttpServletRequest request) throws ServletException {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.addUser(userForm.getUsername(), userForm.getPassword());
        request.login(userForm.getUsername(), userForm.getPassword());

        return "redirect:/attack";
    }
}
