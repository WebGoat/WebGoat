package org.owasp.webgoat.lessons.ssrf;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
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

      // Разрешаем запросы ТОЛЬКО к ifconfig.pro
      if (!"ifconfig.pro".equalsIgnoreCase(parsedUrl.getHost())) {
        return getFailedResult("Access to this URL is not allowed.");
      }

      // Запрещаем локальные IP-адреса
      if (isLocalAddress(parsedUrl)) {
        return getFailedResult("Access to internal resources is forbidden.");
      }

      // Открываем соединение с безопасными настройками
      HttpURLConnection connection = (HttpURLConnection) parsedUrl.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);
      // Запрещаем редиректы
      connection.setInstanceFollowRedirects(false);

      try (InputStream in = connection.getInputStream()) {
        // Форматируем вывод
        String html = new String(in.readAllBytes(), StandardCharsets.UTF_8).replaceAll("\n", "<br>");
        return success(this).feedback("ssrf.success").output(html).build();
      }

    } catch (MalformedURLException e) {
      return getFailedResult("Invalid URL format: " + e.getMessage());
    } catch (IOException e) {
      return getFailedResult("Failed to retrieve content: " + e.getMessage());
    }
  }

  private AttackResult getFailedResult(String errorMsg) {
    return failed(this).feedback("ssrf.failure").output(errorMsg).build();
  }

  private boolean isLocalAddress(URL url) throws IOException {
    String host = url.getHost();
    InetAddress address = InetAddress.getByName(host);

    return address.isLoopbackAddress() || address.isAnyLocalAddress() ||
           address.isSiteLocalAddress() || host.equalsIgnoreCase("localhost");
  }
}
