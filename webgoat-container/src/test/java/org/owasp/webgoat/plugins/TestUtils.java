package org.owasp.webgoat.plugins;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since September 22, 2016
 */
public class TestUtils {

    public static void assertTitlePresent(WebDriver webDriver, String title) {
        FluentWait<WebDriver> wait = new WebDriverWait(webDriver, 15); // wait for a maximum of 15 seconds
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("lesson-title"), title));
    }

    public static  FluentWait createDefaultWait(WebDriver webDriver) {
        return new FluentWait(webDriver)
                .withTimeout(10, SECONDS)
                .pollingEvery(2, SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

    }
}
