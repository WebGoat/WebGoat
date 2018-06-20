package org.owasp.webgoat.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author nbaars
 * @since 3/19/17.
 */
@Getter
@Setter
public class UserForm {

    @NotNull
    @Size(min=6, max=20)
    @Pattern(regexp = "[a-zA-Z0-9-]*", message = "can only contain letters, digits, and -")
    private String username;
    @NotNull
    @Size(min=6, max=10)
    private String password;
    @NotNull
    @Size(min=6, max=10)
    private String matchingPassword;
    @NotNull
    private String agree;
}
