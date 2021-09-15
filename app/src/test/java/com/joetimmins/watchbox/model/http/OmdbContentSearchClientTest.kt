package com.joetimmins.watchbox.model.http

import com.joetimmins.watchbox.CoroutineTestRule
import com.joetimmins.watchbox.enqueueResponse
import com.joetimmins.watchbox.model.ApiSearchResult
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
        mockWebServer.enqueueResponse("search-results.json")

        runBlocking {
            val result = searchClient.search("", "", "")
            assertEquals(
                ApiSearchResult(
                    title = "Twin Peaks",
                    yearRange = "1990–1991",
                    imdbId = "tt0098936",
                    contentType = "series",
                    posterUrl = "https://m.media-amazon.com/images/M/MV5BMTExNzk2NjcxNTNeQTJeQWpwZ15BbWU4MDcxOTczOTIx._V1_SX300.jpg"
                ),
                result.results.first()
            )
        }
    }
}
