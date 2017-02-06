package com.toad.config

import net.sourceforge.tess4j.Tesseract
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author Amber [johnnyven@outlook.com]
 * @since 2017-02-06 22:29
 *
 */
@Configuration
open class Tess4jConfig {

    @Bean
    open fun tess4j():Tesseract {
        return Tesseract()
    }

}