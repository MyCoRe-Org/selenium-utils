/**
 * 
 */
package org.mycore.common.selenium.drivers;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Thomas Scheffler (yagee)
 *
 */
public class MCRRemoteSafariDriverFactory extends MCRRemoteDriverFactory {

    @Override
    public DesiredCapabilities getCapabilities() {
        return DesiredCapabilities.safari();
    }

}
