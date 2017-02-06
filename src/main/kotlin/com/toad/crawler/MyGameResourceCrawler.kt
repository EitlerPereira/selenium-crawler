package com.toad.crawler

import com.toad.util.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 *
 * @author Amber [johnnyven@outlook.com]
 * @since 2017-02-06 23:32
 *
 */
@Component
open class MyGameResourceCrawler(@Autowired val webDriver: WebDriver) {


    open fun crawl() {
        webDriver.get("http://www.2gei.com/view/153.html")
        webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS)
        val executor = Executors.newFixedThreadPool(10)

        val wait = WebDriverWait(webDriver, 10L)

        while (SeleniumUtil.waitForAjaxToLoad(wait, webDriver) && webDriver.findElements(By.linkText("下一页")).isNotEmpty()) {
            val currentHandle = webDriver.windowHandle
            println("main window: $currentHandle")

            crawlPage(currentHandle, executor, wait)

            webDriver.findElements(By.linkText("下一页"))
                    .first()
                    .let {
                        dismissOtherWindow(currentHandle)
                        Actions(webDriver).moveToElement(it).click().perform()
                        Thread.sleep(1500)
                    }
        }
    }

    private fun crawlPage(currentHandle: String?, executor: ExecutorService?, wait: WebDriverWait) {
        webDriver.findElements(By.cssSelector("span.item-info-bottom-down")).forEach {
            CompletableFuture.runAsync(Runnable {

                dismissOtherWindow(currentHandle)

                wait.until(ExpectedConditions.elementToBeClickable(it))
                println(it.text)
                Actions(webDriver).moveToElement(it).click().perform()
            }, executor)

            Thread.sleep(1000)
        }
    }

    private fun dismissOtherWindow(currentHandle: String?) {
        println("total window count: ${webDriver.windowHandles.count()}")
        webDriver.windowHandles
                .filter { it != currentHandle }
                .forEach {
                    println("child window: $it")
                    webDriver.switchTo().window(it)
                    webDriver.close()
                }
        webDriver.switchTo().window(currentHandle)
    }
}