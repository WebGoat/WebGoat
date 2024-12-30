package org.owasp.webgoat;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


@UsePlaywright
public class LoginTest {

    @Test
    void newUser(Page page) {
        var randomUsername = "webgoat-" + UUID.randomUUID();
        var password = "test1234";
        page.navigate("http://localhost:8080/WebGoat/login");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("or register yourself as a new")).click();
        page.getByPlaceholder("Username").fill(randomUsername);
        page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true)).fill(password);
        page.getByLabel("Confirm password").fill(password);
        page.getByLabel("Agree with the terms and").check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign up")).click();
        assertThat(page.locator("[id=\"_what_is_webgoat\"]")).containsText("What is WebGoat?");
    }

    @Test
    void login(Page page) {
        //create user
        var randomUsername = "webgoat-" + UUID.randomUUID();
        var password = "test1234";
        page.navigate("http://localhost:8080/WebGoat/login");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("or register yourself as a new")).click();
        page.getByPlaceholder("Username").fill(randomUsername);
        page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true)).fill(password);
        page.getByLabel("Confirm password").fill(password);
        page.getByLabel("Agree with the terms and").check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign up")).click();
        assertThat(page.locator("[id=\"_what_is_webgoat\"]")).containsText("What is WebGoat?");

        //logout
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("")).click();
        page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Logout")).click();

        //login
        page.navigate("http://localhost:8080/WebGoat/login");
        page.getByPlaceholder("Username").fill(randomUsername);
        page.getByPlaceholder("Password").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        assertThat(page.locator("[id=\"_what_is_webgoat\"]")).containsText("What is WebGoat?");
    }
}
