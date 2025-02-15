

package org.owasp.webgoat.lessons.vulnerablecomponents;

import lombok.Data;

@Data
public class ContactImpl implements Contact {

  private Integer id;
  private String firstName;
  private String lastName;
  private String email;
}
