/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpbasics;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.util.Map;
import java.util.Random;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@AssignmentHints({"http-basics.hints.http_basics_external.1"})
public class HttpBasicsExternal implements AssignmentEndpoint {
    private final LessonSession lessonSession;
    private final Random random;

    public HttpBasicsExternal(LessonSession lessonSession) {
        this.lessonSession = lessonSession;
        this.random = new Random();
    }

    @PostMapping("/HttpBasics/externalcheck")
    @ResponseBody
    public AttackResult completed(@RequestParam String code) {
        if (!code.isBlank() && lessonSession.getValue("external_http_secret").equals(code)) {
            return success(this).build();
        } else {
            return failed(this).build();
        }
    }

    @PutMapping("/HttpBasics/external")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getSecretCode(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        var secret = Integer.toHexString(random.nextInt());
        lessonSession.setValue("external_http_secret", secret);
        if (request.getHeader("User-Agent").equalsIgnoreCase("Attacker") &&
                request.getHeader("Content-Type").equalsIgnoreCase("application/json") &&
                body.containsKey("external") &&
                body.get("external") instanceof Boolean &&
                (boolean) body.get("external") == true)
            return ResponseEntity
                .ok()
                .body(Map.of("secret_code", secret));
        else return ResponseEntity.badRequest().body(Map.of("error","Double check your request and make sure it matches the assignment's requirements"));

    }
}
