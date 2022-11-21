package org.owasp.webgoat.lessons.webwolfintroduction;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Email implements Serializable {

    private String contents;
    private String sender;
    private String title;
    private String recipient;
}
