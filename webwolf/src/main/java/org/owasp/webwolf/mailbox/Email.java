package org.owasp.webwolf.mailbox;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@Data
@Entity
@NoArgsConstructor
public class Email implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;
    @Column(length = 1024)
    private String contents;
    private String sender;
    private String title;
    private String recipient;

    public String getSummary() {
        return "-" + this.contents.substring(0, 50);
    }

    public LocalDateTime getTimestamp() {
        return time;
    }

    public String getTime() {
        return DateTimeFormatter.ofPattern("h:mm a").format(time);
    }

    public String getShortSender() {
        return sender.substring(0, sender.indexOf("@"));
    }

}
