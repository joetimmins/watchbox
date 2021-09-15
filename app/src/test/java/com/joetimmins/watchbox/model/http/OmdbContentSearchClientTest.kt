package com.joetimmins.watchbox.model.http

import com.joetimmins.watchbox.CoroutineTestRule
import com.joetimmins.watchbox.enqueueResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class OmdbContentSearchClientTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()


    private val mockWebServer = MockWebServer()
    private val searchClient = OmdbService.retrofitBuilder(mockWebServer.url("/"))
        .build()
        .create(OmdbContentSearchClient::class.java)

    @Test
    fun `when server sends search response, it is correctly deserialised`() {
        mockWebServer.enqueueResponse("twin-peaks-search-results.json")

        runBlocking {
            val searchResults = searchClient.search("", "", "")
            assertEquals(
                twinPeaksApiSearchResults,
                searchResults
            )
        }
    }
}
