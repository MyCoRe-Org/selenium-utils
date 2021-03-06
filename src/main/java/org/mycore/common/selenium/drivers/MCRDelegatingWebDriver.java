package org.mycore.common.selenium.drivers;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.remote.RemoteWebDriver;

public class MCRDelegatingWebDriver implements WebDriver, JavascriptExecutor,
    FindsById, FindsByClassName, FindsByLinkText, FindsByName,
    FindsByCssSelector, FindsByTagName, FindsByXPath,
    HasInputDevices, HasCapabilities, TakesScreenshot {

    private RemoteWebDriver delegate;

    public MCRDelegatingWebDriver(RemoteWebDriver delegate) {
        this.delegate = delegate;
    }

    public RemoteWebDriver getDelegate() {
        return delegate;
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    public Capabilities getCapabilities() {
        return delegate.getCapabilities();
    }

    public void get(String url) {
        delegate.get(url);
    }

    public String getTitle() {
        return delegate.getTitle();
    }

    public String getCurrentUrl() {
        return delegate.getCurrentUrl();
    }

    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return delegate.getScreenshotAs(outputType);
    }

    public List<WebElement> findElements(By by) {
        return delegate.findElements(by);
    }

    public WebElement findElement(By by) {
        return delegate.findElement(by);
    }

    public WebElement findElementById(String using) {
        return delegate.findElementById(using);
    }

    public List<WebElement> findElementsById(String using) {
        return delegate.findElementsById(using);
    }

    public WebElement findElementByLinkText(String using) {
        return delegate.findElementByLinkText(using);
    }

    public List<WebElement> findElementsByLinkText(String using) {
        return delegate.findElementsByLinkText(using);
    }

    public WebElement findElementByPartialLinkText(String using) {
        return delegate.findElementByPartialLinkText(using);
    }

    public List<WebElement> findElementsByPartialLinkText(String using) {
        return delegate.findElementsByPartialLinkText(using);
    }

    public WebElement findElementByTagName(String using) {
        return delegate.findElementByTagName(using);
    }

    public List<WebElement> findElementsByTagName(String using) {
        return delegate.findElementsByTagName(using);
    }

    public WebElement findElementByName(String using) {
        return delegate.findElementByName(using);
    }

    public List<WebElement> findElementsByName(String using) {
        return delegate.findElementsByName(using);
    }

    public WebElement findElementByClassName(String using) {
        return delegate.findElementByClassName(using);
    }

    public List<WebElement> findElementsByClassName(String using) {
        return delegate.findElementsByClassName(using);
    }

    public WebElement findElementByCssSelector(String using) {
        return delegate.findElementByCssSelector(using);
    }

    public List<WebElement> findElementsByCssSelector(String using) {
        return delegate.findElementsByCssSelector(using);
    }

    public WebElement findElementByXPath(String using) {
        return delegate.findElementByXPath(using);
    }

    public List<WebElement> findElementsByXPath(String using) {
        return delegate.findElementsByXPath(using);
    }

    public String getPageSource() {
        return delegate.getPageSource();
    }

    public void close() {
        delegate.close();
    }

    public void quit() {
        delegate.quit();
    }

    public Set<String> getWindowHandles() {
        return delegate.getWindowHandles();
    }

    public String getWindowHandle() {
        return delegate.getWindowHandle();
    }

    public Object executeScript(String script, Object... args) {
        return delegate.executeScript(script, args);
    }

    public Object executeAsyncScript(String script, Object... args) {
        return delegate.executeAsyncScript(script, args);
    }

    public TargetLocator switchTo() {
        return delegate.switchTo();
    }

    public Navigation navigate() {
        return delegate.navigate();
    }

    public Options manage() {
        return delegate.manage();
    }

    public Keyboard getKeyboard() {
        return delegate.getKeyboard();
    }

    public Mouse getMouse() {
        return delegate.getMouse();
    }

    public String toString() {
        return delegate.toString();
    }

}
