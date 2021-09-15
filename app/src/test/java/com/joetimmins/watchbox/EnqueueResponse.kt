package com.joetimmins.watchbox

import com.joetimmins.watchbox.BufferedFile.Companion.bufferedFile
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.enqueueResponse(filename: String, code: Int = 200) =
    enqueue(
        MockResponse()
            .setBody(filename.bufferedFile())
            .setResponseCode(code)
    )