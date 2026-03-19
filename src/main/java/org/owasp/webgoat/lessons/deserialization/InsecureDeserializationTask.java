package org.owasp.webgoat.lessons.deserialization;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@AssignmentHints({
  "insecure-deserialization.hints.1",
  "insecure-deserialization.hints.2",
  "insecure-deserialization.hints.3"
})
public class InsecureDeserializationTask implements AssignmentEndpoint {

    @PostMapping("/InsecureDeserialization/task")
    @ResponseBody
    public AttackResult completed(@RequestParam String token) throws IOException {
        try {
            String json = new String(Base64.getDecoder().decode(token.replace('-', '+').replace('_', '/')));
            ObjectMapper objectMapper = new ObjectMapper();
            SafeTaskHolder task = objectMapper.readValue(json, SafeTaskHolder.class);
            return success(this).feedback("deserialization.success").build();
        } catch (IOException e) {
            return failed(this).feedback("deserialization.invalid").build();
        }
    }

    // Безопасный класс без выполнения кода при десериализации
    public static class SafeTaskHolder {
        private String task;

        public String getTask() { return task; }
        public void setTask(String task) { this.task = task; }
    }
}
