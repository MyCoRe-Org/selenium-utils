package org.mycore.common.selenium.drivers;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MCRFirefoxDriverFactory extends MCRDriverFactory {

    @Override
    public WebDriver getDriver() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxProfile profile = getFirefoxProfile(Locale.GERMANY);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.configureFromEnv();
        firefoxOptions.setProfile(profile);
        configureLogging(firefoxOptions);

        LogManager.getLogger().info("is headless: {}", isHeadless());
        LogManager.getLogger().info("is debug enabled: {}", isDebugEnabled());
        if (isHeadless()) {
            firefoxOptions.addArguments("--headless");
        }
        GeckoDriverService gecko = new GeckoDriverService.Builder()
            .build();
        gecko.sendOutputTo(System.err);
        FirefoxDriver firefoxDriver = new FirefoxDriver(gecko, firefoxOptions);
        firefoxDriver.manage().window().setSize(new Dimension(dimX, dimY));
        firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return firefoxDriver;
    }

    private void configureLogging(FirefoxOptions firefoxOptions) {
        if (isDebugEnabled()) {
            firefoxOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
        } else {
            firefoxOptions.setLogLevel(FirefoxDriverLogLevel.INFO);
        }
    }

    private FirefoxProfile getFirefoxProfile(Locale locale) {
        FirefoxProfile profile = new FirefoxProfile();
        String formattedLocale = locale.getCountry().isEmpty() ? locale.getLanguage()
            : locale.getLanguage() + "-" + locale.getCountry().toLowerCase(Locale.ROOT);
        profile.setPreference("intl.accept_languages", formattedLocale);
        return profile;
    }
}
