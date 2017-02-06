package com.toad.service

import org.junit.Test

import org.junit.Assert.*

/**
 * @author Amber [johnnyven@outlook.com]
 * *
 * @since 2016-12-25 17:56
 */
class TesseractServiceTest {

    @Test
    fun testExecute() {
        val service = TesseractService()
        service.execute("E:\\captcha\\captcha.jpg", "E:\\captcha\\result.txt")
    }
}