package org.owasp.webgoat.webwolf.requests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;

class WebWolfTraceRepositoryTest {

  @Test
  @DisplayName("When a user hits a file upload it should be recorded")
  void shouldAddFilesRequest() {
    HttpExchange httpExchange = mock();
    HttpExchange.Request request = mock();
    when(httpExchange.getRequest()).thenReturn(request);
    when(request.getUri()).thenReturn(URI.create("http://localhost:9090/files/test1234/test.jpg"));
    WebWolfTraceRepository repository = new WebWolfTraceRepository();

    repository.add(httpExchange);

    Assertions.assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("When a user hits file upload page ('/files') it should be recorded")
  void shouldAddNotAddFilesRequestOverview() {
    HttpExchange httpExchange = mock();
    HttpExchange.Request request = mock();
    when(httpExchange.getRequest()).thenReturn(request);
    when(request.getUri()).thenReturn(URI.create("http://localhost:9090/files"));
    WebWolfTraceRepository repository = new WebWolfTraceRepository();

    repository.add(httpExchange);

    Assertions.assertThat(repository.findAll()).hasSize(0);
  }
}
