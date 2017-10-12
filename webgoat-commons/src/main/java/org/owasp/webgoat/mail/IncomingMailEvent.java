package org.owasp.webgoat.mail;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@Builder
@Data
public class IncomingMailEvent {

    private LocalDateTime time;
    private String contents;
    private String sender;
    private String title;
    private String recipient;
}