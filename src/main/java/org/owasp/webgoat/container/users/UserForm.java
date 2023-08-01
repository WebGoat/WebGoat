package org.owasp.webgoat.container.users;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author nbaars
 * @since 3/19/17.
 */
@Getter
@Setter
public class UserForm {

  @NotNull
  @Size(min = 6, max = 45)
  @Pattern(regexp = "[a-z0-9-]*", message = "can only contain lowercase letters, digits, and -")
  private String username;

  @NotNull
  @Size(min = 6, max = 10)
  private String password;

  @NotNull
  @Size(min = 6, max = 10)
  private String matchingPassword;

  @NotNull private String agree;
}
