package com.toad.util

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

/**
 *
 * @author Amber [johnnyven@outlook.com]
 * @since 2017-02-07 1:13
 *
 */
object SeleniumUtil {
    fun waitForAjaxToLoad(wait: WebDriverWait, driver: WebDriver): Boolean {
        val jQueryLoad = ExpectedCondition {
            try {
                return@ExpectedCondition ((driver as JavascriptExecutor).executeScript("return jQuery.active") as Long) == 0L
            } catch (e: Exception) {
                return@ExpectedCondition true
            }
        }

        val jsLoad = ExpectedCondition {
            return@ExpectedCondition (driver as JavascriptExecutor)
                    .executeScript("return document.readyState")
                    .toString() == "complete"
        }
       return wait.until(jQueryLoad) && wait.until(jsLoad)
    }
}