package com.toad

import org.apache.commons.pool2.impl.GenericObjectPool
import org.junit.Test
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.TimeUnit

@RunWith(SpringRunner::class)
@SpringBootTest
class TumblrCrawlerApplicationTests {

    @Autowired
    private lateinit var driverPool: GenericObjectPool<WebDriver>

    @Test
    fun contextLoads() {
    }

    @Test
    fun testWebDriver() {
        val driver = driverPool.borrowObject()
        driver.get("http://www.2gei.com/view/153.html")
        val startTime = System.currentTimeMillis()
        println(startTime)
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS)
        val endTime = System.currentTimeMillis()
        println(endTime)
        println("等待页面耗时：${endTime - startTime} 毫秒")
        driver.findElements(By.cssSelector("span.item-info-bottom-down")).forEach {
            println(it.text)
            it.click()
            Thread.sleep(3000)

        }
        driverPool.returnObject(driver)

    }
}
