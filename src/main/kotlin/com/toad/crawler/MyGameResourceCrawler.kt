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
    val kingOfDragonUrl = "http://www.2gei.com/view/56099.html" // 龙王url
    val caoCaoZhuanUrl = "http://www.2gei.com/view/153.html" // 曹操传url
    val mainPage = "http://www.2gei.com/"
    val executor: ExecutorService = Executors.newFixedThreadPool(10)

    open fun login(): Boolean {
        webDriver.get(mainPage)
        if (!SeleniumUtil.waitForAjaxToLoad(wait, webDriver)) {
            return false
        }
        val loginBtn = webDriver.findElement(By.id("loginBtn"))
        val mainWindow = webDriver.windowHandle

        Actions(webDriver).moveToElement(loginBtn).click().perform()

        webDriver.windowHandles
                .filter { it != mainWindow }
                .first()
                .let {
                    webDriver.switchTo().window(it)
                    println("title: ${webDriver.title}")
                    if (SeleniumUtil.waitForAjaxToLoad(wait, webDriver)) {
                        val qqPortrait = webDriver.switchTo().frame("ptlogin_iframe").findElement(By.xpath("//*[@id='nick_243634401']"))
                        Actions(webDriver).moveToElement(qqPortrait).click().perform()
                        webDriver.switchTo().window(mainWindow)
                        return true
                    }
                    return false
                }

    }

    private val wait: WebDriverWait by lazy {
        val wait = WebDriverWait(webDriver, 10L)
        wait
    }

    open fun crawl() {
        webDriver.get(kingOfDragonUrl)

        while (SeleniumUtil.waitForAjaxToLoad(wait, webDriver) && webDriver.findElements(By.linkText("下一页")).isNotEmpty()) {
            val currentHandle = webDriver.windowHandle
            println("main window: $currentHandle")

            crawlPage(executor, wait)

            webDriver.findElements(By.linkText("下一页"))
                    .first()
                    .let {
//                        dismissOtherWindow(currentHandle)
                        Actions(webDriver).moveToElement(it).click().perform()
                        Thread.sleep(1500)
                    }
        }

        crawlPage(executor, wait) // crawl last page
    }

    private fun crawlPage(executor: ExecutorService?, wait: WebDriverWait) {
        webDriver.findElements(By.cssSelector("span.item-info-bottom-down")).forEach {
            CompletableFuture.runAsync(Runnable {

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