package org.owasp.webwolf.mailbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@Builder
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {

    @Id
    private String id;
    private LocalDateTime time;
    private String contents;
    private String sender;
    private String title;
    @Indexed
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