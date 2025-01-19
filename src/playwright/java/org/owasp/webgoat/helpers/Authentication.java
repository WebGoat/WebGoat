package org.owasp.webgoat.helpers;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.owasp.webgoat.pages.RegistrationPage;
import org.owasp.webgoat.pages.WebGoatLoginPage;

public class Authentication {

  private record User(String name, String password, String auth) {
    boolean loggedIn() {
      return auth != null;
    }
  }

  private static User sylvester = new User("sylvester", "sylvester", null);
  private static User tweety = new User("tweety", "tweety", null);

  public static String sylvester(Page page) {
    return login(page, sylvester);
  }

  public static String tweety(Page page) {
    return login(page, tweety);
  }

  private static Page login(Page page, User user) {
    if (sylvester.loggedIn()) {
      return sylvester.auth;
    }
    RegistrationPage registrationPage = new RegistrationPage(page);
    registrationPage.open();
    registrationPage.register(sylvester.name, sylvester.password);
    assertThat(registrationPage.getSignUpButton()).not().isVisible();

    WebGoatLoginPage loginPage = new WebGoatLoginPage(page);
    loginPage.open();
    loginPage.login(sylvester.name, sylvester.password);
    assertThat(loginPage.getSignInButton()).not().isVisible();


    sylvester =
        new User(
            sylvester.name,
            sylvester.password,
            page.context().storageState(new BrowserContext.StorageStateOptions()));

    return page.context().storageState(new BrowserContext.StorageStateOptions()).newPage();
  }
}
