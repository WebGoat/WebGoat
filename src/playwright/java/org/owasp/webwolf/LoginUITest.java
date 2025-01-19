package org.owasp.webwolf;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.PlaywrightTest;
import org.owasp.webwolf.pages.WebWolfLoginPage;

public class LoginUITest extends PlaywrightTest {

  @Test
  void login(Page page) {
    var loginPage = new WebWolfLoginPage(page);
    loginPage.open();
    loginPage.login(getUser(), getPassword());

    assertThat(loginPage.getSignInButton()).not().isVisible();

    // logout
    loginPage.logout();

    assertThat(loginPage.getSignInButton()).isVisible();
  }
}
