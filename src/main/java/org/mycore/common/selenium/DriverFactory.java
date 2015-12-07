package org.mycore.common.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public abstract class DriverFactory {

    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);

    private static DriverFactory driverFactoryInstance = getFactory();

    private static final String DRIVER_PROVIDER = "DriverProvider";

    protected int dimX = Integer.parseInt(System.getProperty("dimX", "1280"));

    protected int dimY = Integer.parseInt(System.getProperty("dimY", "1024"));

    public static DriverFactory getFactory() {
        if (driverFactoryInstance == null) {
            String driverName = System.getProperty("DriverProvider", null);
            LOGGER.info("Search for driver in env variables");

            if (driverName != null) {
                LOGGER.info("Driver found in env variable : " + driverName);
            } else {
                driverName = System.getProperty(DRIVER_PROVIDER, "org.mycore.common.selenium.FirefoxDriverFactory");
            }
            LOGGER.info("Load DriverProviderFactory!");
            try {
                driverFactoryInstance = (DriverFactory) Class.forName(driverName).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return driverFactoryInstance;
    }

    public WebDriver getDriver() {
        return driverFactoryInstance.getDriver();
    }
}
