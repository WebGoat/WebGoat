package org.owasp.webgoat.plugins;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


/**
 * Created by dm on 8/21/15.
 */

public class WebGoatIT {
    /*@Test
    public void shouldHavePhantomJsBinary() {
        String binary = System.getProperty("phantomjs.binary");
        assertNotNull(binary);
        assertTrue(new File(binary).exists());
    }*/

    @Test
    public void testTomcatDeployment() {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:8080/WebGoat");

        WebElement usernameElement     = driver.findElement(By.name("username"));
        WebElement passwordElement     = driver.findElement(By.name("password"));
        assertNotNull(usernameElement);
        assertNotNull(passwordElement);
    }

    @Test
    public void testLogin() {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:8080/WebGoat");

        WebElement usernameElement     = driver.findElement(By.name("username"));
        WebElement passwordElement     = driver.findElement(By.name("password"));
        assertNotNull(usernameElement);
        assertNotNull(passwordElement);

        usernameElement.sendKeys("webgoat");
        passwordElement.sendKeys("webgoat");
        passwordElement.submit();

        WebElement cookieParameters = driver.findElement(By.id("cookies-and-params"));
        assertNotNull(cookieParameters);
    }
}
