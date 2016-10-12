package org.owasp.webgoat.plugins;

import com.google.common.base.Predicate;
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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.owasp.webgoat.plugins.TestUtils.assertTitlePresent;
import static org.owasp.webgoat.plugins.TestUtils.createDefaultWait;


/**
 * Created by Doug Morato <dm@corp.io> on 8/21/15.
 */
@RunWith(ConcurrentParameterized.class)
public class WebGoatIT implements SauceOnDemandSessionIdProvider {

    // Since most Tomcat deployments run on port 8080, let's set the automated integration tests to
    // spawn tomcat on port 8888 so that we don't interfere with local Tomcat's
    protected String baseWebGoatUrl = "http://localhost:8888/WebGoat";
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

    protected ThreadLocal<WebDriver> _webDriver = new ThreadLocal<>();
    protected ThreadLocal<String> sessionId = new ThreadLocal<>();


    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     *
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

    public WebDriver getWebDriver() {
        return _webDriver.get();
    }

    public String getSessionId() {
        return sessionId.get();
    }


    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();

        // windows 7, Chrome latest
        //browsers.add(new String[]{"Windows 7", "", "chrome", null, null});

        // windows 10, Chrome latest
        browsers.add(new String[]{"Windows 10", "", "chrome", null, null});

        // Linux, Firefox latest
        browsers.add(new String[]{"Linux", "", "firefox", null, null});

        // windows 10, IE latest
        //browsers.add(new String[]{"Windows 10", "", "internetExplorer", null, null});

        // windows 10, Microsoft Edge Browser latest
        //browsers.add(new String[]{"Windows 10", "", "edge", null, null});

        // OS X 10.11 El Capitan, Safari
        //browsers.add(new String[]{"OSX 10.11", "", "safari", null, null});

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
        capabilities.setCapability("wwebgetWebDriver()RemoteQuietExceptions", false);
        capabilities.setCapability("captureHtml", true);

        if (System.getenv("CI") != null && System.getenv("TRAVIS").equals("true")) {
            capabilities.setCapability("tunnelIdentifier", System.getenv("TRAVIS_JOB_NUMBER"));
            capabilities.setCapability("tags", System.getenv("TRAVIS_PULL_REQUEST"));
            capabilities.setCapability("build", System.getenv("TRAVIS_BUILD_NUMBER"));
        }

        capabilities.setCapability(CapabilityType.PLATFORM, os);

        String methodName = name.getMethodName();
        capabilities.setCapability("name", methodName);

        this._webDriver.set(new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() +
                        "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities));
        this.getWebDriver().manage().timeouts().implicitlyWait(4, SECONDS);
        this.sessionId.set((((RemoteWebDriver) getWebDriver()).getSessionId()).toString());

        String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", this.sessionId, methodName);
        System.out.println(message);
    }

    public void doLoginWebgoatUser() {

        getWebDriver().get(baseWebGoatUrl + "/login.mvc");
        getWebDriver().navigate().refresh();

        WebDriverWait wait = new WebDriverWait(getWebDriver(), 15); // wait for a maximum of 15 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputEmail1")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputPassword1")));

        WebElement usernameElement = getWebDriver().findElement(By.name("username"));
        WebElement passwordElement = getWebDriver().findElement(By.name("password"));
        usernameElement.sendKeys(loginUser);
        passwordElement.sendKeys(loginPassword);
        passwordElement.submit();
        getWebDriver().get(baseWebGoatUrl + "/start.mvc");
    }

    /**
     * Runs a simple test verifying the UI and title of the WebGoat home page.
     *
     * @throws Exception
     */
    @Test
    public void verifyWebGoatLoginPage() throws Exception {
        getWebDriver().get(baseWebGoatUrl + "/login.mvc");
        WebDriverWait wait = new WebDriverWait(getWebDriver(), 15); // wait for a maximum of 15 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputEmail1")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputPassword1")));

        assertTrue(getWebDriver().getTitle().equals("Login Page"));

        WebElement usernameElement = getWebDriver().findElement(By.name("username"));
        WebElement passwordElement = getWebDriver().findElement(By.name("password"));
        assertNotNull(usernameElement);
        assertNotNull(passwordElement);
    }


    @Test
    public void testStartMvc() {
        getWebDriver().get(baseWebGoatUrl + "/start.mvc");

        WebDriverWait wait = new WebDriverWait(getWebDriver(), 15); // wait for a maximum of 15 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
    }

    @Test
    public void testWebGoatUserLogin() {

        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/start.mvc");
        String pageSource = getWebDriver().getPageSource();

        assertTrue("user: webgoat is not in the page source", pageSource.contains("Role: webgoat_admin"));
        WebElement cookieParameters = getWebDriver().findElement(By.id("cookies-and-params"));
        assertNotNull("element id=cookieParameters should be displayed to user upon successful login", cookieParameters);
    }

    @Test
    public void testServiceLessonMenuMVC() {

        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/service/lessonmenu.mvc");

        String pageSource = getWebDriver().getPageSource();

        assertTrue("Page source should contain lessons: Test 1", pageSource.contains("Reflected XSS"));
        assertTrue("Page source should contain lessons: Test 2", pageSource.contains("Access Control Flaws"));
        assertTrue("Page source should contain lessons: Test 34", pageSource.contains("Fail Open Authentication Scheme"));
    }

    @Test
    public void testAccessControlFlaws() {
        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1708534694/200");
        getWebDriver().get(baseWebGoatUrl + "/service/restartlesson.mvc");
        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1708534694/200");

        FluentWait<WebDriver> wait = createDefaultWait(getWebDriver());
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("lesson-title"), "Using an Access Control Matrix"));

        WebElement user = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("User")));
        user.click();
        user.sendKeys("L");

        WebElement resource = getWebDriver().findElement(By.name("Resource"));
        resource.click();
        resource.sendKeys("A");

        WebElement submit = getWebDriver().findElement(By.name("SUBMIT"));
        submit.click();

        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Congratulations");
            }
        });
    }

    @Test
    public void testRoleBasedAccessConrol() throws IOException {
        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/160587164/200");
        getWebDriver().get(baseWebGoatUrl + "/service/restartlesson.mvc");
        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/160587164/200");

        assertTitlePresent(getWebDriver(), "LAB: Role Based Access Control");
        this.getWebDriver().manage().timeouts().implicitlyWait(4, SECONDS);

        FluentWait<WebDriver> wait = createDefaultWait(getWebDriver());
        WebElement user = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("employee_id")));
        user.click();
        user.sendKeys("T");

        WebElement resource = getWebDriver().findElement(By.name("password"));
        resource.click();
        resource.sendKeys("tom");

        WebElement submit = getWebDriver().findElement(By.name("action"));
        submit.click();


        wait = createDefaultWait(getWebDriver());
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Welcome Back");
            }
        });

        JavascriptExecutor javascript = (JavascriptExecutor) getWebDriver();
        String value = "document.getElementsByName('action')[0].value='DeleteProfile';";
        javascript.executeScript(value);

        WebElement viewProfile = getWebDriver().findElements(By.name("action")).get(0);
        viewProfile.click();
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Stage 2");
            }
        });

        //
        // Stage 3
        //
        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/160587164/200/3");
        assertTitlePresent(getWebDriver(), "LAB: Role Based Access Control");

        user = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("employee_id")));
        user.click();
        user.sendKeys("T");

        resource = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
        resource.click();
        resource.sendKeys("tom");

        submit =  wait.until(ExpectedConditions.presenceOfElementLocated(By.name("action")));
        submit.click();

        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Welcome Back");
            }
        });

        javascript = (JavascriptExecutor) getWebDriver();
        value = "var select = document.getElementsByName('employee_id')[0]; select.options[0].value='106'; ";
        javascript.executeScript(value);

        viewProfile = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name("action"))).get(0);
        viewProfile.click();
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Stage 4");
            }
        });
    }

    @Test
    public void testFailOpenAuthenticationScheme() throws IOException {
        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1075773632/200");
        getWebDriver().get(baseWebGoatUrl + "/service/restartlesson.mvc");
        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1075773632/200");

        FluentWait<WebDriver> wait = createDefaultWait(getWebDriver());
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("lesson-title"), "Fail Open Authentication Scheme"));

        WebElement user = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Username")));
        user.click();
        user.sendKeys("Larry");

        JavascriptExecutor javascript = (JavascriptExecutor) getWebDriver();
        String todisable = "document.getElementsByName('Password')[0].setAttribute('disabled', '');";
        javascript.executeScript(todisable);
        assertFalse(getWebDriver().findElement(By.name("Password")).isEnabled());

        WebElement submit = getWebDriver().findElement(By.name("SUBMIT"));
        submit.click();
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Congratulations");
            }
        });
    }

    @Test
    public void testSqlInjectionLabLessonPlanShouldBePresent() throws IOException {
        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1537271095/200");
        getWebDriver().get(baseWebGoatUrl + "/service/restartlesson.mvc");
        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1537271095/200");

        FluentWait<WebDriver> wait = createDefaultWait(getWebDriver());
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("lesson-title"), "LAB: SQL Injection"));

        assertFalse(getWebDriver().getPageSource().contains("Lesson Plan Title: How to Perform a SQL Injection"));
        WebElement user = getWebDriver().findElement(By.id("show-plan-button"));
        user.click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("lesson-plan-content"), "Lesson Plan Title: How to Perform a SQL Injection"));
    }

    //@Test
    public void testClientSideValidation() throws IOException {
        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1129417221/200");
        getWebDriver().get(baseWebGoatUrl + "/service/restartlesson.mvc");
        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1129417221/200");

        FluentWait<WebDriver> wait = createDefaultWait(getWebDriver());
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("lesson-title"), "Insecure Client Storage"));

        getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Stage 1
        WebElement user = getWebDriver().findElement(By.name("field1"));
        user.click();
        user.sendKeys("PLATINUM");

        WebElement submit = getWebDriver().findElement(By.name("SUBMIT"));
        submit.click();
        wait.until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return getWebDriver().getPageSource().contains("Stage 2");
            }
        });

        //Stage 2
        WebElement qty = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("QTY1")));
        qty.click();
        qty.sendKeys("8");
        qty = getWebDriver().findElement(By.name("QTY1"));
        qty.click();
        qty.sendKeys("8");
        getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        JavascriptExecutor javascript = (JavascriptExecutor) getWebDriver();
        String cmd = "document.getElementsByName('GRANDTOT')[0].value = '$0.00';";
        javascript.executeScript(cmd);

        getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        submit = getWebDriver().findElement(By.name("SUBMIT"));
        submit.click();
        wait = new FluentWait(getWebDriver())
                .withTimeout(10, SECONDS)
                .pollingEvery(2, SECONDS)
                .ignoring(NoSuchElementException.class);
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Congratulations");
            }
        });
    }

    @Test
    public void testJavaScriptValidation() throws IOException {
        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1574219258/1700");
        getWebDriver().get(baseWebGoatUrl + "/service/restartlesson.mvc");
        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1574219258/1700");

        FluentWait<WebDriver> wait = createDefaultWait(getWebDriver());
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("lesson-title"), "Bypass Client Side JavaScript Validation"));

        getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        for (int i = 1; i <= 7; i++) {

            WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("field" + i)));
            field.click();
            field.sendKeys("@#@{@#{");
        }

        JavascriptExecutor javascript = (JavascriptExecutor) getWebDriver();
        String cmd = "document.getElementById('submit_btn').onclick=''";
        javascript.executeScript(cmd);

        WebElement submit = getWebDriver().findElement(By.id("submit_btn"));
        submit.click();

        getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Congratulations");
            }
        });
    }

    @Test
    public void testSqlInjectionLabLessonSolutionAreNotAvailable() throws IOException {
        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1537271095/200");
        getWebDriver().get(baseWebGoatUrl + "/service/restartlesson.mvc");
        getWebDriver().get(baseWebGoatUrl + "/start.mvc#attack/1537271095/200");

        FluentWait<WebDriver> wait = createDefaultWait(getWebDriver());
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("lesson-title"), "LAB: SQL Injection"));
        this.getWebDriver().manage().timeouts().implicitlyWait(4, SECONDS);

        WebElement user =  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("show-solution-button")));
        user.click();

        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("Could not find the solution file");
            }
        });
    }

    @Test
    public void testLogoutMvc() {

        doLoginWebgoatUser();

        getWebDriver().get(baseWebGoatUrl + "/logout.mvc");

        assertTrue("Page title should be Logout Page", getWebDriver().getTitle().contains("Logout Page"));
        assertTrue("Logout message should be displayed to user when successful logout",
                getWebDriver().getPageSource().contains("You have logged out successfully"));
    }

    /**
     * Closes the {@link WebDriver} session.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        getWebDriver().quit();
    }

}
