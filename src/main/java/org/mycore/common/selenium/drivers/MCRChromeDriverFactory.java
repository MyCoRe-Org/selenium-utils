package org.mycore.common.selenium.drivers;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MCRChromeDriverFactory extends MCRDriverFactory {

    @Override
    public WebDriver getDriver() {
        ChromeOptions profile = getChromeOptions(Locale.GERMANY);
        ChromeDriver chromeDriver = new ChromeDriver(profile);
        chromeDriver.manage().window().setSize(new Dimension(dimX, dimY));
        chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return chromeDriver;
    }

    private ChromeOptions getChromeOptions(Locale locale) {
        String formattedLocale = locale.getCountry().isEmpty() ? locale.getLanguage()
            : locale.getLanguage() + "-" + locale.getCountry().toLowerCase(Locale.ROOT);
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--lang=" + formattedLocale);
        return opts;
    }
}
