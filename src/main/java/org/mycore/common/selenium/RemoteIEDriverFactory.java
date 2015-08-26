package org.mycore.common.selenium;

import org.openqa.selenium.remote.DesiredCapabilities;

public class RemoteIEDriverFactory extends RemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        return DesiredCapabilities.internetExplorer();
    }
}
