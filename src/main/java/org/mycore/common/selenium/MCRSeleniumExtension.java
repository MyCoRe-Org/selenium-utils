package org.mycore.common.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestWatcher;
import org.mycore.common.selenium.drivers.MCRDriverFactory;
import org.mycore.common.selenium.drivers.MCRRemoteDriverFactory;
import org.mycore.common.selenium.drivers.MCRWebdriverWrapper;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MCRSeleniumExtension implements BeforeAllCallback, AfterAllCallback, TestWatcher, ParameterResolver {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DRIVER_KEY = "selenium.driver";
    private static final String SCREENSHOT_KEY = "selenium.screenshot";
    private static final String HTML_SOURCE_KEY = "selenium.html";
    private static final String TEST_URL_KEY = "selenium.url";

    @Override
    public void beforeAll(ExtensionContext context) {
        MCRWebdriverWrapper driver = new MCRWebdriverWrapper(
            (RemoteWebDriver) MCRDriverFactory.getFactory().getDriver(), 30);
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put(DRIVER_KEY, driver);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        MCRWebdriverWrapper driver = getDriver(context);
        if (driver != null) {
            driver.quit();
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        LOGGER.error("Error in test", cause);

        MCRWebdriverWrapper driver = getDriver(context);
        if (driver == null) {
            return;
        }

        // Take screenshot and capture page data
        takeScreenshot(context, driver);

        String className = context.getRequiredTestClass().getSimpleName();
        String methodName = context.getTestMethod().get().getName();
        String resultFolder = System.getProperty("ResultFolder", "target/result");

        Path failedTestClassDirectory = Path.of(resultFolder, className);
        Path failedTestDirectory = failedTestClassDirectory.resolve(methodName);
        try {
            Files.createDirectories(failedTestDirectory);
            if (cause != null) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    OutputStreamWriter w = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
                    PrintWriter pw = new PrintWriter(w)) {

                    String testUrl = getTestUrl(context);
                    if (testUrl != null) {
                        pw.println(testUrl);
                    }
                    cause.printStackTrace(pw);
                    writeToFile(baos.toByteArray(), failedTestDirectory.resolve("error.txt"), "exception");
                }
            }

            byte[] screenshot = getScreenshot(context);
            writeToFile(screenshot, failedTestDirectory.resolve("screenshot.png"), "screenshot");

            String htmlSource = getHtmlSource(context);
            if (htmlSource != null) {
                writeToFile(htmlSource.getBytes(Charset.defaultCharset()), failedTestDirectory.resolve("dom.html"),
                    "DOM");
            }
        } catch (IOException ioe) {
            LOGGER.error("Error handling test error", ioe);
        }
    }

    public static MCRWebdriverWrapper getDriver(ExtensionContext context) {
        return context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
            .get(DRIVER_KEY, MCRWebdriverWrapper.class);
    }

    public static void takeScreenshot(ExtensionContext context) {
        MCRWebdriverWrapper driver = getDriver(context);
        if (driver != null) {
            takeScreenshot(context, driver);
        }
    }

    private static void takeScreenshot(ExtensionContext context, MCRWebdriverWrapper driver) {
        ExtensionContext.Store store = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);

        String htmlSource = driver.getPageSource();
        String testUrl = driver.getCurrentUrl();
        byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);

        store.put(HTML_SOURCE_KEY, htmlSource);
        store.put(TEST_URL_KEY, testUrl);
        store.put(SCREENSHOT_KEY, screenshot);
    }

    private static byte[] getScreenshot(ExtensionContext context) {
        return context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
            .get(SCREENSHOT_KEY, byte[].class);
    }

    private static String getHtmlSource(ExtensionContext context) {
        return context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
            .get(HTML_SOURCE_KEY, String.class);
    }

    private static String getTestUrl(ExtensionContext context) {
        return context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
            .get(TEST_URL_KEY, String.class);
    }

    private static void writeToFile(byte[] bytes, Path fileName, String type) throws IOException {
        if (bytes == null || bytes.length == 0) {
            LOGGER.error(() -> "Could not save " + type + ". No data given.");
            return;
        }
        try (OutputStream fout = Files.newOutputStream(fileName)) {
            LOGGER.info(() -> "Saving " + type + " to " + fileName);
            fout.write(bytes);
        }
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
                    URL url = URI.create(driverURL).toURL();
                    canonicalHostName = MCRSeleniumTestUtils.getLocalAdress(url.getHost(), url.getPort())
                        .getCanonicalHostName();
                    LOGGER.info("hostname is : {}", canonicalHostName);
                } catch (IOException e) {
                    throw new RuntimeException("could not detect hostname!", e);
                }
            }
            hostName = canonicalHostName;
        }

        String baseUrl;
        try {
            baseUrl = new URI(System.getProperty("UrlScheme", "http://"), null, hostName, baseUrlPort, null, null,
                "").toString();
        } catch (URISyntaxException e) {
            baseUrl = "http://" + hostName + ":" + baseUrlPort;
        }

        return baseUrl;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        Class<?> parameterClass = parameterContext.getParameter().getType();
        return parameterClass.isAssignableFrom(MCRWebdriverWrapper.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return switch (parameterContext.getParameter().getType()) {
            case Class<?> c when c.isAssignableFrom(MCRWebdriverWrapper.class) -> getDriver(extensionContext);
            default -> null;
        };
    }
}
