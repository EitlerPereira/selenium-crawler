package com.toad.config


import org.apache.commons.pool2.PooledObjectFactory
import org.apache.commons.pool2.impl.GenericObjectPool
import org.openqa.selenium.WebDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author Amber [johnnyven@outlook.com]
 * @since 2016-12-25 14:53
 *
 */
@Configuration
open class ChromeDriverConfig {

    @Autowired
    private lateinit var chromeDriverFactory: PooledObjectFactory<WebDriver>


    @Bean
    open fun chromeDriverPool(): GenericObjectPool<WebDriver> {
        val pool = GenericObjectPool(chromeDriverFactory)
        pool.maxTotal = 2
        pool.lifo = false
        pool.blockWhenExhausted = true
        pool.maxWaitMillis = 3000

        return  pool
    }


}