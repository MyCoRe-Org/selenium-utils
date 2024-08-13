package org.mycore.common.selenium.drivers;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MCRRemoteChromeDriverFactory extends MCRRemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        return new DesiredCapabilities(Browser.CHROME.browserName(), "", Platform.ANY);
    }
}
