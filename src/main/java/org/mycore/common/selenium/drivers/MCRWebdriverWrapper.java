/**
 * 
 */
package org.mycore.common.selenium.drivers;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Wraps a {@link RemoteWebDriver} and provides extra utility functions.
 * @author Thomas Scheffler
 */
public class MCRWebdriverWrapper extends MCRDelegatingWebDriver {

    private int timeout;

    public MCRWebdriverWrapper(RemoteWebDriver delegate, int timeout) {
        super(delegate);
        this.timeout = timeout;
    }

    public <R> R waitAnd(Function<By, R> andThen, By by) {
        WebDriverWait wait = new WebDriverWait(getDelegate(), timeout);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        return andThen.apply(by);
    }

    public <R> R waitFor(Supplier<R> supplier) {
        WebDriverWait wait = new WebDriverWait(getDelegate(), timeout);
        return wait.until(giveResult(supplier)::apply);
    }

    private static <T, R> Function<T, R> giveResult(Supplier<R> supplier) {
        return w -> supplier.get();
    }

    public WebElement waitAndFindElement(By by) {
        return waitAnd(getDelegate()::findElement, by);
    }

    public WebElement waitAndFindElement(SearchContext ctx, By by) {
        return waitFor(() -> ctx.findElement(by));
    }

    public List<WebElement> waitAndFindElements(By by) {
        return waitAnd(getDelegate()::findElements, by);
    }

    public List<WebElement> waitAndFindElements(SearchContext ctx, By by) {
        return waitFor(() -> {
            List<WebElement> results = ctx.findElements(by);
            return results.isEmpty() ? null : results;
        });
    }

}
