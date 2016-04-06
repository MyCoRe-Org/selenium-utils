package org.mycore.common.selenium.drivers;

import org.openqa.selenium.remote.DesiredCapabilities;

public class MCRRemoteFirefoxDriverFactory extends MCRRemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        return DesiredCapabilities.firefox();
    }

}
