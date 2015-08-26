package org.mycore.common.selenium;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.*;

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

}
