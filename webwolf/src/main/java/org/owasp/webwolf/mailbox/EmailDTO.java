package org.owasp.webwolf.mailbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private LocalDateTime time = LocalDateTime.now();
    @Column(length = 1024)
    private String contents;
    private String sender;
    private String title;
    private String recipient;
  
  public Email map(){
     Email email = new Email(this.id,this.time,this.contents,this.sender,this.tittle,this.recipient);
     return email;
  }

}
