package org.owasp.webgoat.plugin.challenge3;

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
public class Comment {
    private String user;
    private String dateTime;
    private String comment;
}

