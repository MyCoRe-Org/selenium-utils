package org.mycore.common.selenium.drivers;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class MCRSafariDriverFactory extends MCRDriverFactory {

    @Override
    public WebDriver getDriver() {
        SafariDriver safariDriver = new SafariDriver();
        configureTimeouts(safariDriver);
        safariDriver.manage().window().setSize(new Dimension(dimX, dimY));
        return safariDriver;
    }

}
