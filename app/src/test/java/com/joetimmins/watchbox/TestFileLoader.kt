package com.joetimmins.watchbox

import okio.Buffer
import okio.source
import java.io.File

class BufferedFile {
    companion object {
        fun String.bufferedFile(): Buffer {
            val resource = this@Companion::class.java.classLoader?.getResource(this)!!
            val file = File(resource.path)
            val buffer = Buffer()
            buffer.writeAll(file.source())
            return buffer
        }
    }
}