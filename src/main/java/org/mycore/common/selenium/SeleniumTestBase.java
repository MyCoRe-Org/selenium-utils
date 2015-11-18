package org.mycore.common.selenium;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumTestBase {

    private static final Logger LOGGER = Logger.getLogger(SeleniumTestBase.class);

    protected static WebDriver driver;

    private byte[] screenShot;

    private String sourceHTML;

    private String testURL;

    private String testName;

    protected File testClassDirectory = new File("target/test-classes");

    @Rule
    public TestWatcher errorLogger = new TestWatcher() {

        @Override
        protected void failed(Throwable e, Description description) {
            LOGGER.error("Error in test", e);
            if (description.isTest()) {
                String className = description.getClassName();
                int p = className.lastIndexOf(".");
                if (p > 0 && (p < className.length() - 1))
                    className = className.substring(p + 1);
                String resultFolder = System.getProperty("ResultFolder", "target/result");

                File failedTestClassDirectory = new File(resultFolder, className);
                String child = getTestName();
                if (child == null || child.isEmpty())
                    child = description.getMethodName();
                File failedTestDirectory = new File(failedTestClassDirectory, child);
                failedTestDirectory.mkdirs();
                if (e != null) {
                    File error = new File(failedTestDirectory, "error.txt");
                    try (FileOutputStream fout = new FileOutputStream(error);
                        OutputStreamWriter osw = new OutputStreamWriter(fout, "UTF-8");
                        PrintWriter pw = new PrintWriter(osw)) {
                        pw.println(testURL);
                        e.printStackTrace(pw);
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
                File screenshot = new File(failedTestDirectory, "screenshot.png");
                try (FileOutputStream fout = new FileOutputStream(screenshot)) {
                    System.out.println("Saving screenshot to " + screenshot.getAbsolutePath());
                    fout.write(screenShot);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
                File html = new File(failedTestDirectory, "dom.html");
                try (FileOutputStream fout = new FileOutputStream(html);
                    OutputStreamWriter osw = new OutputStreamWriter(fout, "UTF-8")) {
                    System.out.println("Saving DOM to " + html.getAbsolutePath());
                    osw.write(sourceHTML);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
            super.failed(e, description);
        }
    };

    @BeforeClass
    public static void setUpClass() {
        setUpDriver();
    }

    public static void setUpDriver() {
        setDriver(DriverFactory.getFactory().getDriver());
    }

    @AfterClass
    public static void tearDownClass() {
        driver.quit();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver Driver) {
        driver = Driver;
    }

    public void takeScreenshot() {
        sourceHTML = driver.getPageSource();
        testURL = driver.getCurrentUrl();
        if (driver instanceof TakesScreenshot) {
            screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
    }

    public String getTestName() {
        return testName;
    }

    protected void setTestName(String testName) {
        this.testName = testName;
    }

    private static String canonicalHostName = null;

    public static String getBaseUrl(String baseURLDefaultPort) {
        String hostName = System.getProperty("HostName", "localhost");
        int baseUrlPort = Integer.parseInt(System.getProperty("BaseUrlPort", baseURLDefaultPort));

        String driverURL = System.getProperty(RemoteDriverFactory.DRIVER_URL_PROPERTY_NAME, null);
        if (driverURL != null && System.getProperty("HostName", null) == null) {
            if (canonicalHostName == null) {
                LOGGER.info("RemoteDriverURL is set but not HostName Try to detect hostname of this machine.");
                try {
                    URL url = new URL(driverURL);
                    canonicalHostName = SeleniumTestUtils.getLocalAdress(url.getHost(), url.getPort())
                        .getCanonicalHostName();
                    LOGGER.info("hostname is : " + canonicalHostName);

                } catch (IOException e) {
                    Assert.fail("could not detect hostname!");
                }
            }
            hostName = canonicalHostName;
        }

        String BASE_URL;
        try {
            BASE_URL = new URL(System.getProperty("UrlScheme", "http://"), hostName, baseUrlPort, "").toString();
        } catch (MalformedURLException e) {
            BASE_URL = "http://" + hostName + ":" + baseUrlPort;
        }

        return BASE_URL;
    }
}
