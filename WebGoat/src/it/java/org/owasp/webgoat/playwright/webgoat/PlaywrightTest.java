/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.owasp.webgoat.ServerUrlConfig;

@UsePlaywright(PlaywrightTest.WebGoatOptions.class)
public class PlaywrightTest {

  private static final ServerUrlConfig webGoatUrlConfig = ServerUrlConfig.webGoat();
  private static final ServerUrlConfig webWolfUrlConfig = ServerUrlConfig.webWolf();

  public static class WebGoatOptions implements OptionsFactory {
    @Override
    public Options getOptions() {
      return new Options()
              .setHeadless(true)
              .setContextOptions(getContextOptions());
    }

  }

  protected static Browser.NewContextOptions getContextOptions() {
    return new Browser.NewContextOptions()
            .setLocale("en-US")
            .setBaseURL(webGoatUrlConfig.getBaseUrl());
  }

  public static String webGoatUrl(String path) {
    return webGoatUrlConfig.url(path);
  }

  public static String webWolfURL(String path) {
    return webWolfUrlConfig.url(path);
  }
}
