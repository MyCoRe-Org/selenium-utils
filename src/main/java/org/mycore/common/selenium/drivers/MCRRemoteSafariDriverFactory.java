/**
 * 
 */
package org.mycore.common.selenium.drivers;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Thomas Scheffler (yagee)
 *
 */
public class MCRRemoteSafariDriverFactory extends MCRRemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        return new DesiredCapabilities(Browser.SAFARI.browserName(), "", Platform.MAC);
    }

}
