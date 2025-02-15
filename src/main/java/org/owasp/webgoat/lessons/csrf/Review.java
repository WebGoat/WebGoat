

package org.owasp.webgoat.lessons.csrf;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class Review {
  private String user;
  private String dateTime;
  private String text;
  private Integer stars;
}
