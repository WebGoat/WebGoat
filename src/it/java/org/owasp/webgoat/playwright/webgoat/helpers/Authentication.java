/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.helpers;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import lombok.Getter;
import org.owasp.webgoat.playwright.webgoat.pages.RegistrationPage;
import org.owasp.webgoat.playwright.webgoat.pages.WebGoatLoginPage;
import org.owasp.webgoat.playwright.webwolf.pages.WebWolfLoginPage;

/**
 * Helper class to authenticate users in WebGoat and WebWolf.
 *
 * <p>It provides two users: sylvester and tweety. The users are authenticated by logging in to
 * WebGoat and WebWolf. Once authenticated, the user's authentication token is stored in the browser
 * and reused for subsequent requests.
 */
public class Authentication {

  public record User(String name, String password, String auth) {
    boolean loggedIn() {
      return auth != null;
    }
  }

  @Getter private static volatile User sylvester = new User("sylvester", "sylvester", null);
  @Getter private static volatile User tweety = new User("tweety", "tweety", null);

  private static final Object sylvesterLock = new Object();
  private static final Object tweetyLock = new Object();

  public static Page sylvester(Browser browser) {
    synchronized (sylvesterLock) {
      if (!sylvester.loggedIn()) {
        sylvester = login(browser, sylvester);
      }
      return browser.newContext(new Browser.NewContextOptions().setLocale("en-US").setStorageState(sylvester.auth)).newPage();
    }
  }

  public static Page tweety(Browser browser) {
    synchronized (tweetyLock) {
      if (!tweety.loggedIn()) {
        tweety = login(browser, tweety);
      }
      return browser.newContext(new Browser.NewContextOptions().setLocale("en-US").setStorageState(tweety.auth)).newPage();
    }
  }

  private static User login(Browser browser, User user) {
    if (user.loggedIn()) {
      return user;
    }
    var page = browser.newContext(new Browser.NewContextOptions().setLocale("en-US")).newPage();
    RegistrationPage registrationPage = new RegistrationPage(page);
    registrationPage.open();
    registrationPage.register(user.name, user.password);

    WebGoatLoginPage loginPage = new WebGoatLoginPage(page);
    loginPage.open();
    loginPage.login(user.name, user.password);
    assertThat(loginPage.getSignInButton()).not().isVisible();

    WebWolfLoginPage webWolfLoginPage = new WebWolfLoginPage(page);
    webWolfLoginPage.open();
    webWolfLoginPage.login(user.name, user.password);
    assertThat(loginPage.getSignInButton()).not().isVisible();

    return new User(user.name, user.password, page.context().storageState());
  }
}
