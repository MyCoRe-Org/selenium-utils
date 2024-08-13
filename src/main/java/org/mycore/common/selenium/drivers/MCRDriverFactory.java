package org.mycore.common.selenium.drivers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public abstract class MCRDriverFactory {

    private static final Logger LOGGER = LogManager.getLogger(MCRDriverFactory.class);

    private static MCRDriverFactory driverFactoryInstance = getFactory();

    public static final String DRIVER_PROVIDER = "DriverProvider";

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
            return (MCRDriverFactory) Class.forName(driverName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException("Error while getting driver!", e);
        }
    }

    public WebDriver getDriver() {
        return driverFactoryInstance.getDriver();
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
