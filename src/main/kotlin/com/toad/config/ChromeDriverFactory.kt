package com.toad.config

import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 *
 * @author Amber [johnnyven@outlook.com]
 * @since 2016-12-25 15:55
 *
 */
@Configuration
open class ChromeDriverFactory : BasePooledObjectFactory<WebDriver>() {

    @Value("\${selenium.driver.name}")
    private lateinit var driverName : String

    @Value("\${selenium.driver.path}")
    private lateinit var driverPath: String

    @Value("\${selenium.driver.download-path}")
    lateinit var downloadPath:String



    override fun wrap(obj: WebDriver?): PooledObject<WebDriver> {
        return DefaultPooledObject<WebDriver>(obj)
    }

    override fun create(): WebDriver {
        System.getProperties().setProperty(driverName, driverPath)
        val options = ChromeOptions()
        options.setExperimentalOption("prefs", mapOf(
                "profile.default_content_settings.popups" to "0",
                "download.default_directory" to downloadPath,
                "download.prompt_for_download" to false.toString(),
                "download.directory_upgrade" to true.toString(),
                "safebrowsing.enabled" to true.toString()
        ))
        val cap = DesiredCapabilities.chrome()
        cap.setCapability(ChromeOptions.CAPABILITY, options)

        return ChromeDriver(cap)
    }

    override fun destroyObject(p: PooledObject<WebDriver>?) {
        p?.`object`?.apply {
            close()
            quit()
        }

    }

    override fun passivateObject(p: PooledObject<WebDriver>?) {
        p?.`object`?.manage()?.window()?.size = Dimension(0, 0)
    }

    override fun activateObject(p: PooledObject<WebDriver>?) {
        p?.`object`?.manage()?.window()?.maximize()
    }
}