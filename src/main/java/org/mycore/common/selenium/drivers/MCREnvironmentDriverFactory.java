/**
 *
 */
package org.mycore.common.selenium.drivers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriver;

/**
 * @author Thomas Scheffler (yagee)
 *
 */
public class MCREnvironmentDriverFactory extends MCRDriverFactory {

    public static String ENV_SELENIUM_BROWSER = "SELENIUM_BROWSER";

    public static String ENV_SELENIUM_HEADLESS = "SELENIUM_HEADLESS";

    public static String ENV_SELENIUM_REMOTE_URL = "SELENIUM_REMOTE_URL";

    private Map<Browser, Class<? extends MCRDriverFactory>> localMap;

    private Map<Browser, Class<? extends MCRDriverFactory>> remoteMap;

    private boolean isRemoteDriver;

    private static Browser DEFAULT_BROWSER = Browser.FIREFOX;

    enum Browser {
        CHROME("chrome"), EDGE("MicrosoftEdge"), FIREFOX("firefox"), IE("internet explorer"), SAFARI("safari");
        private String env;

        Browser(String env) {
            this.env = env;
        }

        @Override
        public String toString() {
            return env;
        }

        public static Browser fromEnvValue(String name) {
            return Stream.of(values())
                .filter(b -> b.toString().equals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                    "No browser matches " + name));
        }
    }

    public MCREnvironmentDriverFactory() {
        localMap = new HashMap<>();
        localMap.put(Browser.FIREFOX, MCRFirefoxDriverFactory.class);
        localMap.put(Browser.CHROME, MCRChromeDriverFactory.class);
        localMap.put(Browser.SAFARI, MCRSafariDriverFactory.class);
        remoteMap = new HashMap<>();
        remoteMap.put(Browser.FIREFOX, MCRRemoteFirefoxDriverFactory.class);
        remoteMap.put(Browser.CHROME, MCRRemoteChromeDriverFactory.class);
        remoteMap.put(Browser.SAFARI, MCRRemoteSafariDriverFactory.class);
        remoteMap.put(Browser.IE, MCRRemoteIEDriverFactory.class);
        String remoteURL = System.getenv(ENV_SELENIUM_REMOTE_URL);
        this.isRemoteDriver = remoteURL != null && remoteURL.length() > 0;
        if (isRemoteDriver && System.getProperty(MCRRemoteDriverFactory.DRIVER_URL_PROPERTY_NAME) == null) {
            System.setProperty(MCRRemoteDriverFactory.DRIVER_URL_PROPERTY_NAME, System.getenv(ENV_SELENIUM_REMOTE_URL));
        }
        boolean headless = Optional.ofNullable(System.getenv(ENV_SELENIUM_HEADLESS))
            .map(Boolean::parseBoolean)
            .orElse(true);
        setHeadless(headless);
        LogManager.getLogger().info("{}={}", ENV_SELENIUM_BROWSER, System.getenv(ENV_SELENIUM_BROWSER));
        LogManager.getLogger().info("{}={}", ENV_SELENIUM_HEADLESS, System.getenv(ENV_SELENIUM_HEADLESS));
        LogManager.getLogger().info("{}={}", ENV_SELENIUM_REMOTE_URL, System.getenv(ENV_SELENIUM_REMOTE_URL));
    }

    private Map<Browser, Class<? extends MCRDriverFactory>> getDriverMap() {
        return isRemoteDriver() ? remoteMap : localMap;
    }

    private boolean isRemoteDriver() {
        return isRemoteDriver;
    }

    private Class<? extends MCRDriverFactory> getDriverFactory() {
        String browserEnv = System.getenv(ENV_SELENIUM_BROWSER);
        Browser browser;
        if (browserEnv != null && browserEnv.length() > 0) {
            browser = Browser.fromEnvValue(browserEnv);
        } else {
            LogManager.getLogger().info("Environment variable {} is undefined. Defaulting to '{}'",
                ENV_SELENIUM_BROWSER, DEFAULT_BROWSER.toString());
            browser = DEFAULT_BROWSER;
        }
        return getDriverMap().computeIfAbsent(browser, b -> {
            String browserList = getDriverMap().keySet().stream().map(Browser::toString)
                .collect(Collectors.joining(", "));
            throw new RuntimeException(
                "Browser " + browserEnv + " is not supported. Supported browsers are: " + browserList);
        });
    }

    @Override
    public WebDriver getDriver() {
        MCRDriverFactory driverFactory = MCRDriverFactory.getDriverFactory(getDriverFactory().getName());
        driverFactory.setHeadless(isHeadless());
        return driverFactory.getDriver();
    }

}
