/**
 * 
 */
package org.mycore.common.selenium.drivers;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.mycore.common.selenium.util.MCRExpectedConditions;
import org.mycore.common.selenium.util.MCRExpectedConditions.DocumentReadyState;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Wraps a {@link RemoteWebDriver} and provides extra utility functions.
 * @author Thomas Scheffler
 */
public class MCRWebdriverWrapper extends MCRDelegatingWebDriver {

    /**
     * System property to set the polling interval of every {@link WebDriverWait} in milliseconds.
     * Defaults to {@link #DEFAULT_POLLING_INTERVAL_MILLIS}.
     * <p>
     * Selenium defaults to 500 ms. As a condition is always evaluated <em>before</em> the first sleep, this
     * interval is only paid by conditions that do not hold immediately. Lower values speed those up at the cost of
     * more round-trips to the browser, which may not pay off against a remote Grid.
     */
    public static final String POLLING_INTERVAL_PROPERTY = "MCR.Selenium.PollingInterval";

    public static final long DEFAULT_POLLING_INTERVAL_MILLIS = 100;

    private final Duration timeout;

    private final Duration pollingInterval;

    public MCRWebdriverWrapper(RemoteWebDriver delegate, int timeout) {
        this(delegate, Duration.ofSeconds(timeout), getConfiguredPollingInterval());
    }

    public MCRWebdriverWrapper(RemoteWebDriver delegate, Duration timeout, Duration pollingInterval) {
        super(delegate);
        this.timeout = timeout;
        this.pollingInterval = pollingInterval;
    }

    private static Duration getConfiguredPollingInterval() {
        return Duration.ofMillis(Long.getLong(POLLING_INTERVAL_PROPERTY, DEFAULT_POLLING_INTERVAL_MILLIS));
    }

    public Duration getTimeout() {
        return timeout;
    }

    public Duration getPollingInterval() {
        return pollingInterval;
    }

    /**
     * Creates the {@link WebDriverWait} instance backing every <code>waitFor…</code> method of this class.
     * Override to customize waiting behaviour.
     */
    protected WebDriverWait newWait() {
        return new WebDriverWait(getDelegate(), timeout, pollingInterval);
    }

    @SafeVarargs
    public final <R> R waitAnd(Function<By, R> andThen, By by, Function<By, ExpectedCondition<?>>... conditions) {
        WebDriverWait wait = newWait();
        ExpectedCondition<?> cond;
        if (conditions == null || conditions.length == 0) {
            cond = ExpectedConditions.presenceOfAllElementsLocatedBy(by);
        } else {
            cond = conditions.length == 1 ? conditions[0].apply(by)
                : ExpectedConditions.and(MCRExpectedConditions.combine(by, conditions));
        }
        wait.until(cond);
        return andThen.apply(by);
    }

    public final <R> R waitFor(Supplier<R> supplier) {
        WebDriverWait wait = newWait();
        return wait.until(giveResult(supplier)::apply);
    }

    public final <R> R waitFor(ExpectedCondition<R> condition) {
        WebDriverWait wait = newWait();
        return wait.until(condition);
    }

    private static <R> ExpectedCondition<R> giveResult(Supplier<R> supplier) {
        return w -> supplier.get();
    }

    @SafeVarargs
    public final WebElement waitAndFindElement(By by, Function<By, ExpectedCondition<?>>... conditions) {
        return waitAnd(getDelegate()::findElement, by, conditions);
    }

    public final WebElement waitAndFindElement(SearchContext ctx, By by) {
        return waitFor(() -> ctx.findElement(by));
    }

    @SafeVarargs
    public final List<WebElement> waitAndFindElements(By by, Function<By, ExpectedCondition<?>>... conditions) {
        return waitAnd(getDelegate()::findElements, by, conditions);
    }

    public List<WebElement> waitAndFindElements(SearchContext ctx, By by) {
        return waitFor(() -> Optional.of(ctx.findElements(by))
            .filter(l -> !l.isEmpty())
            .orElse(null));
    }

    public boolean waitUntilPageIsLoaded(String title) {
        return waitFor(ExpectedConditions.and(
            ExpectedConditions.titleContains(title),
            MCRExpectedConditions.documentReadyState(DocumentReadyState.complete)));
    }

}
