package org.mycore.common.selenium.drivers;

import java.util.HashMap;
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
            : locale.getLanguage() + "-" + locale.getCountry().toLowerCase(Locale.ROOT) + "," + locale.getLanguage();
        ChromeOptions opts = new ChromeOptions();
        HashMap<String, String> prefs = new HashMap<String, String>();
        prefs.put("intl.accept_languages", formattedLocale);
        opts.setExperimentalOption("prefs", prefs);
        return opts;
    }
}
