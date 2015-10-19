package org.mycore.common.selenium;

import java.text.MessageFormat;
import org.openqa.selenium.By;

/**
 * @author Sebastian Röher (basti890)
 *
 */
public class MyBy {

    public static By byLinkTextIgnoreCSS(String linkText) {
        return By.xpath(linkToXPathExpression(linkText));
    }

    public static By byLinkContainsIgnoreCSS(String contains) {
        return By.xpath(linkContainsToXPathExpression(contains));
    }

    private static String linkContainsToXPathExpression(String contains) {
        return MessageFormat.format("//a[contains(text()=\"{0}\")] | //a//*[contains(text()=\"{0}\")]", contains);
    }

    private static String linkToXPathExpression(String linkText) {
        return MessageFormat.format("//a[normalize-space(text())=\"{0}\"] | //a//*[normalize-space(text())=\"{0}\"]",
            linkText);
    }

    public static By byTextIgnoreCSS(String text) {
        return By.xpath(MessageFormat.format("//*[contains(normalize-space(text()),\"{0}\")]", text));
    }

}
