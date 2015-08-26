package org.mycore.common.selenium;

import org.openqa.selenium.remote.DesiredCapabilities;

public class RemoteFirefoxDriverFactory extends RemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        return DesiredCapabilities.firefox();
    }

}
