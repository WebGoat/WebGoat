package org.owasp.webgoat.plugins;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by Doug Morato <dm@corp.io> on 8/21/15.
 *
 */
@RunWith(ConcurrentParameterized.class)
public class WebGoatIT implements SauceOnDemandSessionIdProvider {

    // Since most Tomcat deployments run on port 8080, let's set the automated integration tests to
    // spawn tomcat on port 8888 so that we don't interfere with local Tomcat's
    private String baseWebGoatUrl = "http://localhost:8888/WebGoat";
    private String loginUser = "webgoat";
    private String loginPassword = "webgoat";

    // Sauce Labs settings
    public String username = System.getenv("SAUCE_USER_NAME") != null ? System.getenv("SAUCE_USER_NAME") : System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_API_KEY") != null ? System.getenv("SAUCE_API_KEY") : System.getenv("SAUCE_ACCESS_KEY");

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s : (%s %s %s)", super.getMethodName(), os, browser, version);
        }
    };

    /**
     * Represents the browser to be used as part of the test run.
     */
    private String browser;
    /**
     * Represents the operating system to be used as part of the test run.
     */
    private String os;
    /**
     * Represents the version of the browser to be used as part of the test run.
     */
    private String version;
    /**
     * Represents the deviceName of mobile device
     */
    private String deviceName;
    /**
     * Represents the device-orientation of mobile device
     */
    private String deviceOrientation;
    /**
     * Instance variable which contains the Sauce Job Id.
     */
    private String sessionId;

    /**
     * The {@link WebDriver} instance which is used to perform browser interactions with.
     */
    private WebDriver driver;


    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param os
     * @param version
     * @param browser
     * @param deviceName
     * @param deviceOrientation
     */

    public WebGoatIT(String os, String version, String browser, String deviceName, String deviceOrientation) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.deviceOrientation = deviceOrientation;
    }

    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();

        // windows 7, Chrome 45
        browsers.add(new String[]{"Windows 7", "45", "chrome", null, null});

        // windows 7, IE 9
        //browsers.add(new String[]{"Windows 7", "9", "internet explorer", null, null});

        // windows 8, IE 10
        //browsers.add(new String[]{"Windows 8", "10", "internet explorer", null, null});

        // windows 8.1, IE 11
        //browsers.add(new String[]{"Windows 8.1", "11", "internet explorer", null, null});

        // windows 10, Microsoft Edge Browser
        //browsers.add(new String[]{"Windows 10", "20.10240", "microsoftedge", null, null});

        // OS X 10.9, Safari 7
        //browsers.add(new String[]{"OSX 10.9", "7", "safari", null, null});

        // OS X 10.10, Safari
        //browsers.add(new String[]{"OSX 10.10", "8", "safari", null, null});

        // OS X 10.11, Safari
        //browsers.add(new String[]{"OSX 10.11", "8.1", "safari", null, null});

        // Linux, Firefox 37
        browsers.add(new String[]{"Linux", "37", "firefox", null, null});

        return browsers;
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (browser != null) capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        if (version != null) capabilities.setCapability(CapabilityType.VERSION, version);
        if (deviceName != null) capabilities.setCapability("deviceName", deviceName);
        if (deviceOrientation != null) capabilities.setCapability("device-orientation", deviceOrientation);

        // Additional settings to help debugging and improve job perf
        capabilities.setCapability("public", "share");
        capabilities.setCapability("wwebdriverRemoteQuietExceptions", false);
        capabilities.setCapability("captureHtml", true);

        if ( System.getenv("CI") != null && System.getenv("TRAVIS").equals("true")) {
            capabilities.setCapability("tunnelIdentifier", System.getenv("TRAVIS_JOB_NUMBER"));
            capabilities.setCapability("tags", System.getenv("TRAVIS_PULL_REQUEST"));
            capabilities.setCapability("build", System.getenv("TRAVIS_BUILD_NUMBER"));
        }

        capabilities.setCapability(CapabilityType.PLATFORM, os);

        String methodName = name.getMethodName();
        capabilities.setCapability("name", methodName);

        this.driver = new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() +
                        "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();

        String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", this.sessionId, methodName);
        System.out.println(message);
    }

    public void doLoginWebgoatUser() {

        driver.get(baseWebGoatUrl + "/login.mvc");
        driver.navigate().refresh();

        WebDriverWait wait = new WebDriverWait(driver, 15); // wait for a maximum of 15 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputEmail1")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputPassword1")));

        WebElement usernameElement     = driver.findElement(By.name("username"));
        WebElement passwordElement     = driver.findElement(By.name("password"));
        usernameElement.sendKeys(loginUser);
        passwordElement.sendKeys(loginPassword);
        passwordElement.submit();
        driver.get(baseWebGoatUrl + "/start.mvc");
    }

    /**
     * Runs a simple test verifying the UI and title of the WebGoat home page.
     * @throws Exception
     */
    @Test
    public void verifyWebGoatLoginPage() throws Exception {
        driver.get(baseWebGoatUrl + "/login.mvc");
        WebDriverWait wait = new WebDriverWait(driver, 15); // wait for a maximum of 15 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputEmail1")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputPassword1")));

        assertTrue(driver.getTitle().equals("Login Page"));

        WebElement usernameElement     = driver.findElement(By.name("username"));
        WebElement passwordElement     = driver.findElement(By.name("password"));
        assertNotNull(usernameElement);
        assertNotNull(passwordElement);
    }


    @Test
    public void testStartMvc() {
        driver.get(baseWebGoatUrl + "/start.mvc");

        WebDriverWait wait = new WebDriverWait(driver, 15); // wait for a maximum of 15 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
    }

    @Test
    public void testWebGoatUserLogin() {

        doLoginWebgoatUser();

        driver.get(baseWebGoatUrl + "/start.mvc");
        String pageSource = driver.getPageSource();

        assertTrue("user: webgoat is not in the page source", pageSource.contains("Role: webgoat_admin"));
        WebElement cookieParameters = driver.findElement(By.id("cookies-and-params"));
        assertNotNull("element id=cookieParameters should be displayed to user upon successful login", cookieParameters);
    }

    @Test
    public void testServiceLessonMenuMVC() {

        doLoginWebgoatUser();

        driver.get(baseWebGoatUrl + "/service/lessonmenu.mvc");

        String pageSource = driver.getPageSource();

        assertTrue("Page source should contain lessons: Test 1", pageSource.contains("Reflected XSS"));
        assertTrue("Page source should contain lessons: Test 2", pageSource.contains("Access Control Flaws"));
        assertTrue("Page source should contain lessons: Test 3", pageSource.contains("Improper Error Handling"));
        assertTrue("Page source should contain lessons: Test 34", pageSource.contains("Fail Open Authentication Scheme"));
    }

    @Test
    public void testLogoutMvc() {

        doLoginWebgoatUser();

        driver.get(baseWebGoatUrl + "/logout.mvc");

        assertTrue("Page title should be Logout Page", driver.getTitle().contains("Logout Page"));
        assertTrue("Logout message should be displayed to user when successful logout", driver.getPageSource().contains("You have logged out successfully"));
    }

    /**
     * Closes the {@link WebDriver} session.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    /**
     *
     * @return the value of the Sauce Job id.
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }
}
