package org.mycore.common.selenium;

import org.openqa.selenium.remote.DesiredCapabilities;

public class RemoteChromeDriverFactory extends RemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        return DesiredCapabilities.chrome();
    }
}
