package org.owasp.webwolf.mailbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
public class EmailDTO{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDTO;
    @JsonIgnore
    private LocalDateTime timeDTO = LocalDateTime.now();
    @Column(length = 1024)
    private String contentsDTO;
    private String senderDTO;
    private String titleDTO;
    private String recipientDTO;
  
  public Email map(){
     Email email = new Email(this.idDTO, this.timeDTO, this.contentsDTO, this.senderDTO, this.titleDTO, this.recipientDTO);
     return email;
  }

}
