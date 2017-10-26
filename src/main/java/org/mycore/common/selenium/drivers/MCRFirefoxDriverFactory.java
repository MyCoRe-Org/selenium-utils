package org.mycore.common.selenium.drivers;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class MCRFirefoxDriverFactory extends MCRDriverFactory {

    @Override
    public WebDriver getDriver() {
        FirefoxProfile profile = getFirefoxProfile(Locale.GERMANY);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(profile);
        LogManager.getLogger().info("is headless: {}", isHeadless());
        firefoxOptions.setHeadless(isHeadless());
        FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
        firefoxDriver.manage().window().setSize(new Dimension(dimX, dimY));
        firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return firefoxDriver;
    }

    private FirefoxProfile getFirefoxProfile(Locale locale) {
        FirefoxProfile profile = new FirefoxProfile();
        String formattedLocale = locale.getCountry().isEmpty() ? locale.getLanguage()
                : locale.getLanguage() + "-" + locale.getCountry().toLowerCase(Locale.ROOT);
        profile.setPreference("intl.accept_languages", formattedLocale);
        return profile;
    }
}
