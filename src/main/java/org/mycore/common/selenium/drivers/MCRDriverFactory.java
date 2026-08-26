package org.mycore.common.selenium.drivers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

public abstract class MCRDriverFactory {

    private static final Logger LOGGER = LogManager.getLogger(MCRDriverFactory.class);

    private static MCRDriverFactory driverFactoryInstance = getFactory();

    public static final String DRIVER_PROVIDER = "DriverProvider";

    /**
     * System property to set the implicit wait of every driver in milliseconds. Defaults to
     * {@link #DEFAULT_IMPLICIT_WAIT_MILLIS}.
     * <p>
     * Selenium advises against mixing implicit and explicit waits: an implicit wait makes
     * {@link WebDriver#findElements(org.openqa.selenium.By)} block in the browser until the timeout expires, which
     * stalls the explicit conditions used by
     * {@link MCRWebdriverWrapper#waitAndFindElement(org.openqa.selenium.By, java.util.function.Function...)}. Tests
     * that call <code>findElement</code> directly instead of going through the wrapper may still rely on it and can
     * restore the former behaviour by setting this property to <code>10000</code>.
     */
    public static final String IMPLICIT_WAIT_PROPERTY = "MCR.Selenium.ImplicitWait";

    public static final long DEFAULT_IMPLICIT_WAIT_MILLIS = 0;

    protected boolean headless = false;

    protected boolean debugEnabled = false;

    protected int dimX = Integer.parseInt(System.getProperty("dimX", "1280"));

    protected int dimY = Integer.parseInt(System.getProperty("dimY", "1024"));

    public static MCRDriverFactory getFactory() {
        if (driverFactoryInstance == null) {
            String driverName = System.getProperty("DriverProvider", null);
            driverFactoryInstance = getDriverFactory(driverName);
        }
        return driverFactoryInstance;
    }

    static MCRDriverFactory getDriverFactory(String driverName) {
        LOGGER.info("Search for driver in env variables");

        if (driverName != null) {
            LOGGER.info("Driver found in env variable : " + driverName);
        } else {
            driverName = System.getProperty(DRIVER_PROVIDER, MCREnvironmentDriverFactory.class.getName());
        }
        LOGGER.info("Load DriverProviderFactory!");
        try {
            return (MCRDriverFactory) Class.forName(driverName).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException |
            InvocationTargetException e) {
            throw new RuntimeException("Error while getting driver!", e);
        }
    }

    public WebDriver getDriver() {
        return driverFactoryInstance.getDriver();
    }

    public static Duration getImplicitWaitTimeout() {
        return Duration.ofMillis(Long.getLong(IMPLICIT_WAIT_PROPERTY, DEFAULT_IMPLICIT_WAIT_MILLIS));
    }

    /**
     * Applies the implicit wait configured via {@link #IMPLICIT_WAIT_PROPERTY} to the given driver.
     */
    protected static void configureTimeouts(WebDriver driver) {
        Duration implicitWait = getImplicitWaitTimeout();
        LOGGER.info("Setting implicit wait to {}", implicitWait);
        driver.manage().timeouts().implicitlyWait(implicitWait);
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

}
