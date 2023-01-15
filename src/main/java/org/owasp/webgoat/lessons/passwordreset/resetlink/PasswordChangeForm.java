package org.owasp.webgoat.lessons.passwordreset.resetlink;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author nbaars
 * @since 8/18/17.
 */
@Getter
@Setter
public class PasswordChangeForm {

  @NotNull
  @Size(min = 6, max = 10)
  private String password;

  private String resetLink;
}
