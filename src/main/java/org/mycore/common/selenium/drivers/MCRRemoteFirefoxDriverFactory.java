package org.mycore.common.selenium.drivers;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MCRRemoteFirefoxDriverFactory extends MCRRemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities(
            Browser.FIREFOX.browserName(),
            "",
            Platform.ANY);
        capabilities.setCapability("acceptInsecureCerts", true);
        return capabilities;
    }

}
