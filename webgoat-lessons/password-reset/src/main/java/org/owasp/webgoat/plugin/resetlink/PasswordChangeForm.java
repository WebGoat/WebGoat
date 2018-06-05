package org.owasp.webgoat.plugin.resetlink;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author nbaars
 * @since 8/18/17.
 */
@Getter
@Setter
public class PasswordChangeForm {

    @NotNull
    @Size(min=6, max=10)
    private String password;
    private String resetLink;

}
