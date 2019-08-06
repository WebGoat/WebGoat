package org.owasp.webgoat.plugin;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@Builder
@Data
public class Email implements Serializable {

    private LocalDateTime time;
    private String contents;
    private String sender;
    private String title;
    private String recipient;
}