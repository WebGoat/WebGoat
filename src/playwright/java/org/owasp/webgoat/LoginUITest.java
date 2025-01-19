package org.owasp.webgoat;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.pages.WebGoatLoginPage;

class LoginUITest extends PlaywrightTest {

    @Test
    void login(Page page) {
        var loginPage = new WebGoatLoginPage(page);
        loginPage.open();
        loginPage.login(getUser(), getPassword());

        assertThat(page.getByRole(AriaRole.CODE)).containsText(getUser());

        //logout
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("")).click();
        page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Logout")).click();

        assertThat(loginPage.getSignInButton()).isVisible();
    }
}
