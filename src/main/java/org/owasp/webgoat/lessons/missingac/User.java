package org.owasp.webgoat.lessons.missingac;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  private String username;
  private String password;
  private boolean admin;
}
