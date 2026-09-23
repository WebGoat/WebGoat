/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpparameterpollution;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "http-parameter-pollution.hints.observation.1",
  "http-parameter-pollution.hints.observation.2"
})
public class ParameterPollutionObservation implements AssignmentEndpoint {

  @GetMapping("/HttpParameterPollution/parameters")
  @ResponseBody
  public AttackResult showParameters(HttpServletRequest request) {
    String[] values = request.getParameterValues("name");
    String firstValue = values == null || values.length == 0 ? "No value" : values[0];
    String allValues = values == null ? "[]" : Arrays.toString(values);
    int valueCount = values == null ? 0 : values.length;
    String output =
        "<p>The request contains <strong>"
            + valueCount
            + "</strong> values named <code>name</code>.</p>"
            + "<p>A component that expects one value reads: <code>"
            + escapeHtml4(firstValue)
            + "</code></p><p>A component that accepts every value reads: <code>"
            + escapeHtml4(allValues)
            + "</code></p>";

    boolean hasDifferentValues =
        values != null
            && values.length > 1
            && Arrays.stream(values).skip(1).anyMatch(value -> !values[0].equals(value));

    if (hasDifferentValues) {
      return success(this)
          .feedback("http-parameter-pollution.observation.success")
          .output(output)
          .build();
    }
    return failed(this)
        .feedback("http-parameter-pollution.observation.try-again")
        .output(output)
        .build();
  }
}
