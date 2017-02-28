package com.toad.config


import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${selenium.driver.chrome.name}")
    private lateinit var driverName : String

    @Value("\${selenium.driver.chrome.path}")
    private lateinit var driverPath: String

    @Value("\${selenium.driver.chrome.download-path}")
    lateinit var downloadPath:String

    @Bean
    open fun webDriver(): WebDriver {
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


}