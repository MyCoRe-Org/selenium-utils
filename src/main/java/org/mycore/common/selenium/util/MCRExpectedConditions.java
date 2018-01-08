package org.mycore.common.selenium.util;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MCRExpectedConditions {

    public static enum DocumentReadyState {
        loading, interactive, complete;
    }

    public static ExpectedCondition<?> combine(By by,
        @SuppressWarnings("unchecked") Function<By, ExpectedCondition<?>>... conditions) {
        return ExpectedConditions.and(Stream.of(conditions)
            .map(c -> c.apply(by))
            .toArray(ExpectedCondition[]::new));
    }

    public static ExpectedCondition<Boolean> documentReadyState(DocumentReadyState state) {
        return new ExpectedCondition<Boolean>() {

            private DocumentReadyState readyState;

            @Override
            public Boolean apply(WebDriver driver) {
                this.readyState = DocumentReadyState.valueOf(
                    ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .toString());
                return readyState == state;
            }

            @Override
            public String toString() {
                return String
                    .format(Locale.ENGLISH, "document.readyState to be \"%s\". Current readyState: \"%s\"", state,
                        readyState);
            }
        };
    }

}
