package org.mycore.common.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public abstract class RemoteDriverFactory extends DriverFactory {

    public static final String DRIVER_URL_PROPERTY_NAME = "remoteDriverURL";

    private static final Logger LOGGER = Logger.getLogger(RemoteDriverFactory.class);

    public static final String DEFAULT_URL = "http://localhost:4444";

    @Override
    public WebDriver getDriver() {
        WebDriver remoteDriver = null;
        try {
            String driverLocation = System.getProperty(DRIVER_URL_PROPERTY_NAME, DEFAULT_URL);
            LOGGER.info(String.format("%s is : %s", DRIVER_URL_PROPERTY_NAME, driverLocation));
            remoteDriver = new RemoteWebDriver(new URL(driverLocation), getCapabilities());
        } catch (MalformedURLException e) {
            LOGGER.error("error while resolving firefox driver location", e);
        }
        remoteDriver.manage().window().setSize(new Dimension(dimX, dimY));
        remoteDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return remoteDriver;
    }

    public abstract DesiredCapabilities getCapabilities();
}
