package org.mycore.common.selenium.drivers;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.HasDownloads;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.ScriptKey;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.federatedcredentialmanagement.FederatedCredentialManagementDialog;
import org.openqa.selenium.federatedcredentialmanagement.HasFederatedCredentialManagement;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.virtualauthenticator.HasVirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MCRDelegatingWebDriver
    implements WebDriver, JavascriptExecutor, HasCapabilities, HasDownloads, HasFederatedCredentialManagement,
    HasVirtualAuthenticator, Interactive, PrintsPage, TakesScreenshot {

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

    @Override
    public Capabilities getCapabilities() {
        return delegate.getCapabilities();
    }

    @Override
    public void get(String url) {
        delegate.get(url);
    }

    @Override
    public String getTitle() {
        return delegate.getTitle();
    }

    @Override
    public String getCurrentUrl() {
        return delegate.getCurrentUrl();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return delegate.getScreenshotAs(outputType);
    }

    @Override
    public Pdf print(PrintOptions printOptions) throws WebDriverException {
        return delegate.print(printOptions);
    }

    @Override
    public WebElement findElement(By locator) {
        return delegate.findElement(locator);
    }

    @Override
    public List<WebElement> findElements(By locator) {
        return delegate.findElements(locator);
    }

    @Override
    public String getPageSource() {
        return delegate.getPageSource();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public void quit() {
        delegate.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return delegate.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return delegate.getWindowHandle();
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return delegate.executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return delegate.executeAsyncScript(script, args);
    }

    @Override
    public TargetLocator switchTo() {
        return delegate.switchTo();
    }

    @Override
    public Navigation navigate() {
        return delegate.navigate();
    }

    @Override
    public Options manage() {
        return delegate.manage();
    }

    @Override
    public void perform(Collection<Sequence> actions) {
        delegate.perform(actions);
    }

    @Override
    public void resetInputState() {
        delegate.resetInputState();
    }

    @Override
    public VirtualAuthenticator addVirtualAuthenticator(VirtualAuthenticatorOptions options) {
        return delegate.addVirtualAuthenticator(options);
    }

    @Override
    public void removeVirtualAuthenticator(VirtualAuthenticator authenticator) {
        delegate.removeVirtualAuthenticator(authenticator);
    }

    @Override
    public List<String> getDownloadableFiles() {
        return delegate.getDownloadableFiles();
    }

    @Override
    public void downloadFile(String fileName, Path targetLocation) throws IOException {
        delegate.downloadFile(fileName, targetLocation);
    }

    @Override
    public void deleteDownloadableFiles() {
        delegate.deleteDownloadableFiles();
    }

    @Override
    public void setDelayEnabled(boolean enabled) {
        delegate.setDelayEnabled(enabled);
    }

    @Override
    public void resetCooldown() {
        delegate.resetCooldown();
    }

    @Override
    public FederatedCredentialManagementDialog getFederatedCredentialManagementDialog() {
        return delegate.getFederatedCredentialManagementDialog();
    }

    public void setFileDetector(FileDetector detector) {
        delegate.setFileDetector(detector);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public ScriptKey pin(String script) {
        return delegate.pin(script);
    }

    @Override
    public void unpin(ScriptKey key) {
        delegate.unpin(key);
    }

    @Override
    public Set<ScriptKey> getPinnedScripts() {
        return delegate.getPinnedScripts();
    }

    @Override
    public Object executeScript(ScriptKey key, Object... args) {
        return delegate.executeScript(key, args);
    }

    @Override
    public void requireDownloadsEnabled(Capabilities capabilities) {
        delegate.requireDownloadsEnabled(capabilities);
    }
}
