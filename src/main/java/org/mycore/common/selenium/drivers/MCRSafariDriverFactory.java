package org.mycore.common.selenium.drivers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class MCRSafariDriverFactory extends MCRDriverFactory {

    @Override
    public WebDriver getDriver() {
        SafariDriver safariDriver = new SafariDriver();
        safariDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        safariDriver.manage().window().setSize(new Dimension(dimX, dimY));
        return safariDriver;
    }

}
