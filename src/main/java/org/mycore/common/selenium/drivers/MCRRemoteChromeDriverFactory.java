package org.mycore.common.selenium.drivers;

import org.openqa.selenium.remote.DesiredCapabilities;

public class MCRRemoteChromeDriverFactory extends MCRRemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        return DesiredCapabilities.chrome();
    }
}
