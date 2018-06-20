package org.mycore.common.selenium;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mycore.common.selenium.drivers.MCRDriverFactory;
import org.mycore.common.selenium.drivers.MCRRemoteDriverFactory;
import org.mycore.common.selenium.drivers.MCRWebdriverWrapper;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

public class MCRSeleniumTestBase {

    private static final Logger LOGGER = LogManager.getLogger(MCRSeleniumTestBase.class);

    protected static MCRWebdriverWrapper driver;

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
                try {
                    if (e != null) {
                        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            PrintWriter pw = new PrintWriter(baos);) {
                            pw.println(testURL);
                            e.printStackTrace(pw);
                            writeToFile(baos.toByteArray(), new File(failedTestDirectory, "error.txt"), "exception");
                        }
                    }
                    writeToFile(screenShot, new File(failedTestDirectory, "screenshot.png"), "screenshot");
                    writeToFile(sourceHTML.getBytes(Charset.defaultCharset()),
                        new File(failedTestDirectory, "dom.html"), "DOM");
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                }
            }
            super.failed(e, description);
        }
    };

    private static void writeToFile(byte[] bytes, File fileName, String type) throws IOException {
        if (bytes == null || bytes.length == 0) {
            System.err.println("Could not save " + type + ". No data given.");
            return;
        }
        try (FileOutputStream fout = new FileOutputStream(fileName)) {
            System.out.println("Saving " + type + " to " + fileName.getAbsolutePath());
            fout.write(bytes);
        }
    }

    @BeforeClass
    public static void setUpClass() {
        setUpDriver();
    }

    public static void setUpDriver() {
        setDriver(new MCRWebdriverWrapper((RemoteWebDriver) MCRDriverFactory.getFactory().getDriver(), 30));
    }

    @AfterClass
    public static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    public MCRWebdriverWrapper getDriver() {
        return driver;
    }

    public static void setDriver(MCRWebdriverWrapper Driver) {
        driver = Driver;
    }

    public void takeScreenshot() {
        sourceHTML = driver.getPageSource();
        testURL = driver.getCurrentUrl();
        screenShot = driver.getScreenshotAs(OutputType.BYTES);
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

        String driverURL = System.getProperty(MCRRemoteDriverFactory.DRIVER_URL_PROPERTY_NAME, null);
        if (driverURL != null && System.getProperty("HostName", null) == null) {
            if (canonicalHostName == null) {
                LOGGER.info("RemoteDriverURL is set but not HostName Try to detect hostname of this machine.");
                try {
                    URL url = new URL(driverURL);
                    canonicalHostName = MCRSeleniumTestUtils.getLocalAdress(url.getHost(), url.getPort())
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
