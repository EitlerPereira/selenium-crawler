package com.toad

import com.toad.crawler.MyGameResourceCrawler
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
    private lateinit var driver: WebDriver

    @Autowired
    private lateinit var crawler: MyGameResourceCrawler

    @Test
    fun contextLoads() {
    }

    @Test
    fun testResourceCrawler() {
        crawler.crawl()
    }
}
