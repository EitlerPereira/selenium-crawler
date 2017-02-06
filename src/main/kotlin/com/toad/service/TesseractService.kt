package com.toad.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.text.MessageFormat


/**
 *
 * @author Amber [johnnyven@outlook.com]
 * @since 2016-12-25 17:29
 *
 */
@Service
class TesseractService {
    private val runtime = Runtime.getRuntime()
    private val cmdTarget = "D:\\Tesseract-OCR\\tesseract.exe {0} {1}"


    fun execute(imagePath: String, destPath: String): String {
        val cmd = MessageFormat.format(cmdTarget, imagePath, destPath)

        val p = runtime.exec(cmd)


        val result = if (p.waitFor() != 0) {
            ""
        } else {
            readResult(destPath)
        }
        return result
    }

    private fun readResult(resultPath: String): String {
        val path = Paths.get(resultPath)

        if (path.toFile() == null) {
            return ""
        }
        return Files.readAllLines(path)
                .reduce { res, line -> res + line }

    }


}