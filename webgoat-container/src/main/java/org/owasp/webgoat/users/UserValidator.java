package org.owasp.webgoat.users;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author nbaars
 * @since 3/19/17.
 */
@Component
public class UserValidator implements Validator {

//    @Autowired
//    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserForm userForm = (UserForm) o;

//        if (userService.findByUsername(userForm.getUsername()) != null) {
//            errors.rejectValue("username", "Duplicate.userForm.username");
//        }

        if (!userForm.getMatchingPassword().equals(userForm.getPassword())) {
            errors.rejectValue("matchingPassword", "password.diff");
        }
    }
}
