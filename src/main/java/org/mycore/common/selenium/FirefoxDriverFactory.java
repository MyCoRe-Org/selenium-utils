package org.mycore.common.selenium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class FirefoxDriverFactory extends DriverFactory {

    @Override
    public WebDriver getDriver() {
        FirefoxDriver firefoxDriver = new FirefoxDriver();
        firefoxDriver.manage().window().setSize(new Dimension(dimX, dimY));
        firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return firefoxDriver;
    }
}
