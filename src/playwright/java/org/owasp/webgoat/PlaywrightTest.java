package org.owasp.webgoat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import lombok.Getter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.owasp.webgoat.pages.WebGoatLoginPage;
import org.owasp.webgoat.pages.RegistrationPage;
import org.owasp.webwolf.pages.WebWolfLoginPage;

import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(PlaywrightTest.WebGoatOptions.class)
public class PlaywrightTest {

  private static String webGoatPort =
      System.getenv().getOrDefault("WEBGOAT_PORT", "").isEmpty()
          ? "8080"
          : System.getenv("WEBGOAT_PORT");
  private static String webWolfPort =
      System.getenv().getOrDefault("WEBWOLF_PORT", "").isEmpty()
          ? "9090"
          : System.getenv("WEBWOLF_PORT");

  @Getter private static final String user = "webgoat-" + UUID.randomUUID();
  @Getter private static final String password = "test1234";

  public static class WebGoatOptions implements OptionsFactory {
    @Override
    public Options getOptions() {
      return new Options().setHeadless(false).setContextOptions(getContextOptions());
    }
  }

  protected static Browser.NewContextOptions getContextOptions() {
    return new Browser.NewContextOptions().setBaseURL("http://localhost:%s".formatted(webGoatPort));
  }

  @BeforeAll
  static void setup(Browser browser) {
    var page = browser.newContext(getContextOptions()).newPage();
    var registrationPage = new RegistrationPage(page);
    registrationPage.open();
    registrationPage.register(getUser(), getPassword());

    assertThat(registrationPage.getSignUpButton()).not().isVisible();
  }

  @BeforeEach
  void login(Page page) {
    var webGoatLoginPage = new WebGoatLoginPage(page);
    webGoatLoginPage.open();
    webGoatLoginPage.login(getUser(), getPassword());

    var webWolfLoginPage = new WebWolfLoginPage(page);
    webWolfLoginPage.open();
    webWolfLoginPage.login(getUser(), getPassword());
  }

  public static String webGoatURL(String path) {
    return "http://localhost:%s/WebGoat/%s".formatted(webGoatPort, path);
  }

  public static String webWolfURL(String path) {
    return "http://localhost:%s/WebWolf/%s".formatted(webWolfPort, path);
  }
}
