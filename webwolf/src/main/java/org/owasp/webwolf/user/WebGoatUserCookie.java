package org.owasp.webwolf.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebGoatUserCookie implements Serializable {

    @Id
    private String username;
    private String cookie;
}
