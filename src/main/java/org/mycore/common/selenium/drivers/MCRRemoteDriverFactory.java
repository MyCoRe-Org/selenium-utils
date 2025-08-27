package org.mycore.common.selenium.drivers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public abstract class MCRRemoteDriverFactory extends MCRDriverFactory {

    public static final String DRIVER_URL_PROPERTY_NAME = "RemoteDriverURL";

    private static final Logger LOGGER = LogManager.getLogger(MCRRemoteDriverFactory.class);

    public static final String DEFAULT_URL = "http://localhost:4444";

    @Override
    public WebDriver getDriver() {
        WebDriver remoteDriver = null;
        try {
            String driverLocation = System.getProperty(DRIVER_URL_PROPERTY_NAME, DEFAULT_URL);
            LOGGER.info("{} is : {}", DRIVER_URL_PROPERTY_NAME, driverLocation);
            remoteDriver = new RemoteWebDriver((new URI(driverLocation)).toURL(), getCapabilities());
        } catch (MalformedURLException | URISyntaxException e) {
            LOGGER.error("error while resolving firefox driver location", e);
        }
        remoteDriver.manage().window().setSize(new Dimension(dimX, dimY));
        remoteDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        ((RemoteWebDriver) remoteDriver).setFileDetector(new LocalFileDetector());
        return remoteDriver;
    }

    public abstract DesiredCapabilities getCapabilities();
}
