package org.owasp.webgoat.lessons.ssrf;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"ssrf.hint3"})
public class SSRFTask2 implements AssignmentEndpoint {

    @PostMapping("/SSRF/task2")
    @ResponseBody
    public AttackResult completed(@RequestParam String url) {
        return furBall(url);
    }

    protected AttackResult furBall(String url) {
        try {
            URL parsedUrl = new URL(url);

            // Έλεγχος αν το domain είναι ακριβώς "ifconfig.pro"
            if (!parsedUrl.getHost().equalsIgnoreCase("ifconfig.pro")) {
                return getFailedResult("<img class=\"image\" alt=\"image post\" src=\"images/cat.jpg\">");
            }

            // Προστασία: έλεγχος αν η IP δεν είναι local
            InetAddress address = InetAddress.getByName(parsedUrl.getHost());
            if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isSiteLocalAddress()) {
                return getFailedResult("Access to local/internal addresses is forbidden.");
            }

            String html;
            try (InputStream in = parsedUrl.openStream()) {
                html = new String(in.readAllBytes(), StandardCharsets.UTF_8)
                        .replaceAll("\n", "<br>");
            } catch (IOException e) {
                html = "<html><body>Although the http://ifconfig.pro site is down, you still managed to solve"
                        + " this exercise the right way!</body></html>";
            }

            return success(this).feedback("ssrf.success").output(html).build();

        } catch (MalformedURLException e) {
            return getFailedResult("Invalid URL format.");
        } catch (UnknownHostException e) {
            return getFailedResult("Unknown host.");
        }
    }

    private AttackResult getFailedResult(String errorMsg) {
        return failed(this).feedback("ssrf.failure").output(errorMsg).build();
    }
}

