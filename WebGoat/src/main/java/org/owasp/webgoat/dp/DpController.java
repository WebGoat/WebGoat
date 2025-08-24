package org.owasp.webgoat.dp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dp")
public class DpController {

    // --- Command injection harness ---
    @PostMapping("/cmd")
    public ResponseEntity<String> runCmd(@RequestParam("cmd") String cmd) {
        // TO./mvnw -q clean package -DskipTestsDO: replace with your real UnsafeCmd invocation
        // e.g. return ResponseEntity.ok(UnsafeCmd.run(cmd));
        return ResponseEntity.ok("echo: " + cmd);
    }

    // --- SQL injection harness ---
    @GetMapping("/sql2")
    public ResponseEntity<String> runSql(@RequestParam("id") String id) {
        // TODO: replace with your real UnsafeSql2 invocation
        // e.g. return ResponseEntity.ok(UnsafeSql2.query(id));
        return ResponseEntity.ok("id=" + id);
    }
}
