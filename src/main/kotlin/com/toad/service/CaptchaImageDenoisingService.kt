package com.toad.service

import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 *
 * @author Amber [johnnyven@outlook.com]
 * @since 2016-12-25 19:38
 *
 */

object ClearImageHelper {

    /**

     * @param srcFile
     * *            需要去噪的图像
     * *
     * @param destDir
     * *            去噪后的图像保存地址
     * *
     * @throws IOException
     */
    fun cleanImage(srcFile: File, destDir: String) {
        val destF = File(destDir)
        if (!destF.exists()) {
            destF.mkdirs()
        }

        val bufferedImage = ImageIO.read(srcFile)
        // 灰度化
        val gray = graying(bufferedImage)
        // 二值化
        val binaryBufferedImage = binarization(bufferedImage, gray)

        // 矩阵打印
//        printPixels(binaryBufferedImage, bufferedImage)

        ImageIO.write(binaryBufferedImage, "jpg", File(destDir, srcFile.name))
    }

    private fun printPixels(binaryBufferedImage: BufferedImage, bufferedImage: BufferedImage) {
        for (y in 0..bufferedImage.height - 1) {
            for (x in 0..bufferedImage.width - 1) {
                if (isBlack(binaryBufferedImage.getRGB(x, y))) {
                    print("*")
                } else {
                    print(" ")
                }
            }
            println()
        }
    }

    private fun binarization(bufferedImage: BufferedImage, gray: Array<IntArray>): BufferedImage {
        val threshold = ostu(gray, bufferedImage.width, bufferedImage.height)
        val binaryBufferedImage = BufferedImage(bufferedImage.width, bufferedImage.height,
                BufferedImage.TYPE_BYTE_BINARY)
        for (x in 0..bufferedImage.width - 1) {
            for (y in 0..bufferedImage.height - 1) {
                if (gray[x][y] > threshold) {
                    gray[x][y] = gray[x][y] or 0x00FFFF
                } else {
                    gray[x][y] = gray[x][y] and 0xFF0000
                }
                binaryBufferedImage.setRGB(x, y, gray[x][y])
            }
        }
        return binaryBufferedImage
    }

    private fun graying(bufferedImage: BufferedImage): Array<IntArray> {
        val gray = Array(bufferedImage.width) { IntArray(bufferedImage.height) }
        for (x in 0..bufferedImage.width - 1) {
            for (y in 0..bufferedImage.height - 1) {
                val argb = bufferedImage.getRGB(x, y)
                // 图像加亮（调整亮度识别率非常高）
                var r = ((argb shr 16 and 0xFF) * 1.1 + 30).toInt()
                var g = ((argb shr 8 and 0xFF) * 1.1 + 30).toInt()
                var b = ((argb shr 0 and 0xFF) * 1.1 + 30).toInt()
                if (r >= 255) {
                    r = 255
                }
                if (g >= 255) {
                    g = 255
                }
                if (b >= 255) {
                    b = 255
                }
                gray[x][y] = Math.pow(Math.pow(r.toDouble(), 2.2) * 0.2973 + Math.pow(g.toDouble(), 2.2) * 0.6274 + Math.pow(b.toDouble(), 2.2) * 0.0753, 1 / 2.2).toInt()
            }
        }
        return gray
    }

    fun isBlack(colorInt: Int): Boolean {
        val color = Color(colorInt)
        if (color.red + color.green + color.blue <= 300) {
            return true
        }
        return false
    }

    fun isWhite(colorInt: Int): Boolean {
        val color = Color(colorInt)
        if (color.red + color.green + color.blue > 300) {
            return true
        }
        return false
    }

    fun isBlackOrWhite(colorInt: Int): Int {
        if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730) {
            return 1
        }
        return 0
    }

    fun getColorBright(colorInt: Int): Int {
        val color = Color(colorInt)
        return color.red + color.green + color.blue
    }

    fun ostu(gray: Array<IntArray>, w: Int, h: Int): Int {
        val histData = IntArray(w * h)
        // Calculate histogram
        for (x in 0..w - 1) {
            for (y in 0..h - 1) {
                val red = 0xFF and gray[x][y]
                histData[red]++
            }
        }

        // Total number of pixels
        val total = w * h

        var sum = 0f
        for (t in 0..255)
            sum += (t * histData[t]).toFloat()

        var sumB = 0f
        var wB = 0
        var wF = 0

        var varMax = 0f
        var threshold = 0

        for (t in 0..255) {
            wB += histData[t] // Weight Background
            if (wB == 0)
                continue

            wF = total - wB // Weight Foreground
            if (wF == 0)
                break

            sumB += (t * histData[t]).toFloat()

            val mB = sumB / wB // Mean Background
            val mF = (sum - sumB) / wF // Mean Foreground

            // Calculate Between Class Variance
            val varBetween = wB.toFloat() * wF.toFloat() * (mB - mF) * (mB - mF)

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween
                threshold = t
            }
        }

        return threshold
    }
}