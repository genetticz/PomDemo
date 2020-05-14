package base;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reporting.ExtentManager;
import reporting.ExtentTestManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BrowserDriver {

    //ITestContext returns all the context of a test run. This method sets the directory and sets the class variable
    //ITestResult returns the result for each test run

    //ExtentReport
    public static ExtentReports extent;
    @BeforeSuite
    public void extentSetup(ITestContext context) {
        ExtentManager.setOutputDirectory(context);
        // creating a new Extentreport
        extent = ExtentManager.getInstance();
    }
    @BeforeMethod
    public void startExtent(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName().toLowerCase();
        //returns an instance of ExtentTest starts a test of methodname
        ExtentTestManager.startTest(method.getName());
        //stores category via ExtentTest method assignCategory sets classname as category for methodname test
        ExtentTestManager.getTest().assignCategory(className);
    }
    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @AfterMethod
    public void afterEachTestMethod(ITestResult result) {
        //this method is returning an object of ExtentTest then accessing the actual .getTest method from ExtentMethod
        ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
        ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));

        System.out.println(result.getMethod().getGroups().length + " amount of getGroups");
        for (String group : result.getMethod().getGroups()) {
            System.out.println("These are the groups " + group);
            ExtentTestManager.getTest().assignCategory(group);
        }

        if (result.getStatus() == 1) {
            // ExtentTest .log is used for setting pass,fail, or skip
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
        } else if (result.getStatus() == 2) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        }
        //calls the EndTest instance endTest
        ExtentTestManager.endTest();
        // .flush is used to wipe extent clean so it can be used for another
        extent.flush();
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(driver, result.getName());
        }
        driver.quit();
    }
    @AfterSuite
    public void generateReport() {
        //important to close out to not hog memory
        extent.close();
    }
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }


    public static WebDriver driver = null;
    public String chosenSheet;
     /**
     * Browserstack Credentials
     *
     * */
    public static final String browserstack_username = System.getProperty("bsUName");
    public static final String browserstack_automateKey = System.getProperty("bsSecretKey");

    // public static final String browserstack_username = "mislam1"; //mislam1
    // public static final String browserstack_automateKey = ""; //


     /**
     *
     * Saucelab Credentials
     *
     * */
    public static final String saucelabs_username = null;
    public static final String saucelabs_accesskey = null;

    /**
     * @parameter tag takes input from our
     * */
    @Parameters({"useCloudEnv","cloudEnvName", "os","os_version", "browserName", "browserVersion", "url"})
    @BeforeMethod
    public void setUp(@Optional("false") boolean useCloudEnv, @Optional("xyz") String cloudEnvName,
                      @Optional("mac") String os, @Optional("10") String os_version, @Optional("firefox") String browserName, @Optional("34")
                              String browserVersion, @Optional("https://www.google.com") String url) throws IOException {


        if (useCloudEnv == true) {
            if (cloudEnvName.equalsIgnoreCase("browserstack")) {
                getCloudDriver(cloudEnvName, browserstack_username, browserstack_automateKey, os, os_version, browserName, browserVersion);
            } else if (cloudEnvName.equalsIgnoreCase("saucelabs")) {
                getCloudDriver(cloudEnvName, saucelabs_username, saucelabs_accesskey, os, os_version, browserName, browserVersion);
            }
        } else {
            getLocalDriver(browserName, os);
        }

        //getLocalDriver(browserName, os);
        //timer to wait for elements to load on page
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // 60
        //amount of time for a page to load completely
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS); //45
        //driver.manage().window().maximize();
        driver.get(url);
        chosenSheet = System.getProperty("excelsheet");
    }

    public WebDriver getLocalDriver(String browserName, String os) {

          // if (browserName == "Chrome")
        if (browserName.equalsIgnoreCase("chrome")) {

            /**
             * Command Line Arguments
             * https://peter.sh/experiments/chromium-command-line-switches/
             * */

            ChromeOptions options =new ChromeOptions();
            // options.setHeadless(true);
            options.addArguments("--start-maximized");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--incognito");

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            if (os.equalsIgnoreCase("windows")) {
                System.setProperty("webdriver.chrome.driver", "../generic/drivers/windows/chromedriver.exe");
                driver = new ChromeDriver(options);
                //  TestLogger.log("Chrome Browser Launched");
            } else if (os.equalsIgnoreCase("mac")) {
                System.setProperty("webdriver.chrome.driver", "../generic/drivers/mac/chromedriver");
                driver = new ChromeDriver(options);
                // TestLogger.log("Chrome Browser Launched");
            }
        }
        else if (browserName.equalsIgnoreCase("firefox")) {
            /**
             * https://chercher.tech/java/chrome-firefox-options-selenium-webdriver
             *
             * */
            FirefoxOptions options = new FirefoxOptions();
            //options.setHeadless(true);
            options.addArguments("--start-maximized");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--private");
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);


            if (os.equalsIgnoreCase("windows")) {
                System.setProperty("webdriver.gecko.driver", "../generic/drivers/windows/geckodriver.exe");
                driver = new FirefoxDriver(options);
            } else if (os.equalsIgnoreCase("mac")) {
                System.setProperty("webdriver.gecko.driver", "./generic/drivers/mac/geckodriver");
                driver = new FirefoxDriver(options);
            }
        }

        return driver;
    }

    public WebDriver getCloudDriver(String envName, String envUsername, String envAccessKey, String os, String os_version, String browserName,
                                    String browserVersion) throws IOException {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browser", browserName);
        caps.setCapability("browser_version", browserVersion);
        caps.setCapability("os", os);
        caps.setCapability("os_version", os_version);

        // WebDriver driver = new RemoteWebDriver(new URL(URL), caps);

        if (envName.equalsIgnoreCase("Saucelabs")) {
            driver = new RemoteWebDriver(new URL("http://" + envUsername + ":" + envAccessKey + "@ondemand.saucelabs.com:80/wd/hub"), caps);

        } else if (envName.equalsIgnoreCase("Browserstack")) {

            caps.setCapability("resolution", "1024x768");
            //  public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
            driver = new RemoteWebDriver(new URL("https://" + envUsername + ":" + envAccessKey + "@hub-cloud.browserstack.com/wd/hub"), caps);
        }
        return driver;
    }

    @AfterMethod
    public void closeOut() {
        //driver.manage().deleteAllCookies();
        driver.quit();
    }
    public static void captureScreenshot(WebDriver driver, String screenshotName){

        DateFormat df = new SimpleDateFormat("(MM.dd.yyyy-HH:mma)");
        Date date = new Date();
        df.format(date);

        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(System.getProperty("user.dir")+ "/screenshots/"+screenshotName+" "+df.format(date)+".png"));
            System.out.println("Screenshot captured");
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot "+e.getMessage());;
        }

    }
}
