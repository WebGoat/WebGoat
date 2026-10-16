/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import org.owasp.webgoat.ServerUrlConfig;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.RequestOptions;

import lombok.Getter;

@Getter
public class HttpBasicsLessonPage extends LessonPage {

    private final Locator enterYourName;
    private final Locator code;
    private final Locator goButton;
    private final Locator submitButton;

    public HttpBasicsLessonPage(Page page) {
        super(page);
        enterYourName = page.locator("input[name=\"person\"]");
        code = page.locator("input[name=\"code\"]");
        goButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Go!"));
        submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
    }

    public Locator getTitle() {
        return getPage()
                .getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("HTTP Basics"));
    }

    public APIResponse requestToExternal(String data, String contentType, String userAgent) {
        var options = RequestOptions.create();
        if (data != null)
            options = options.setData(data);
        if (contentType != null)
            options = options.setHeader("Content-Type", contentType);
        if (userAgent != null)
            options = options.setHeader("User-Agent", userAgent);
        return this.getPage().request().put(ServerUrlConfig.webGoat().url("HttpBasics/external"),
                options);
    }

    public void solveQuiz(boolean correct) {
        Page page = getPage();
        page.waitForSelector("#q_container input[name='question_0_solution']");
        page.locator("#question_0_2_input").check();
        page.locator("#question_1_3_input").check();
        page.locator("#question_2_1_input").check();
        page.locator("#question_3_2_input").check();
        page.locator("#question_4_3_input").check();
        page.locator("#question_5_4_input").check();
        if (!correct) {
            page.locator("#question_6_3_input").check();
        } else {
            page.locator("#question_6_4_input").check();
        }

        page.locator("#quiz-form input[type='SUBMIT']").click();
    }
}
