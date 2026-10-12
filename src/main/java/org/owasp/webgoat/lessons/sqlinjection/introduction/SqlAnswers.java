package org.owasp.webgoat.lessons.sqlinjection.introduction;

import java.util.Locale;
import java.util.Set;

/**
 * Helper for the "type a SQL statement" assignments. User input is never executed; it is only
 * compared against a fixed set of accepted answers, after which a constant statement is run.
 */
final class SqlAnswers {

  private SqlAnswers() {}

  static String normalize(String query) {
    if (query == null) {
      return "";
    }
    String q = query.trim().replace('"', '\'').replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    q = q.replaceAll("\\s*=\\s*", "=").replaceAll("\\s*,\\s*", ",");
    while (q.endsWith(";")) {
      q = q.substring(0, q.length() - 1).trim();
    }
    return q;
  }

  static boolean matches(String query, Set<String> acceptedAnswers) {
    String normalized = normalize(query);
    return acceptedAnswers.stream().map(SqlAnswers::normalize).anyMatch(normalized::equals);
  }
}
