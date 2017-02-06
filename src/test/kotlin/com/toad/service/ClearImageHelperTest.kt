package com.toad.service

import org.junit.Test

import org.junit.Assert.*
import java.nio.file.Paths

/**
 * @author Amber [johnnyven@outlook.com]
 * *
 * @since 2016-12-25 20:16
 */
class ClearImageHelperTest {

    @Test
    fun testCleanImage() {
        ClearImageHelper.cleanImage(Paths.get("E:\\captcha\\captcha.jpg").toFile(), "E:\\captcha\\cleaned.jpg")
    }
}