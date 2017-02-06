package com.toad

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class TumblrCrawlerApplication {
    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(TumblrCrawlerApplication::class.java, *args)
        }
    }

}
