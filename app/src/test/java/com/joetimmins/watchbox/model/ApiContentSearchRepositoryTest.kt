package com.joetimmins.watchbox.model

import app.cash.turbine.Event
import app.cash.turbine.test
import com.joetimmins.watchbox.CoroutineTestRule
import com.joetimmins.watchbox.model.http.OmdbContentSearchClient
import com.joetimmins.watchbox.model.http.twinPeaksApiSearchResults
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ApiContentSearchRepositoryTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @Test
    fun `when collecting results flow, and search term is not entered, result is no search performed`() {
        val repository = contentSearchRepository(successfulSearchClient)

        runBlocking {
            repository.searchResults.test {
                val allEmissions = cancelAndConsumeRemainingEvents()
                    .filterIsInstance<Event.Item<ContentSearchResults>>()
                    .map { it.value }
                assertEquals(1, allEmissions.size)
                assertEquals(ContentSearchResults.NoSearchPerformed, allEmissions.first())
            }
        }
    }

    @Test
    fun `given search call succeeds, when collecting results flow, and search term less than three characters long is entered, result is no search performed`() {
        val repository = contentSearchRepository(successfulSearchClient)

        runBlocking {
            repository.searchResults.test {
                repository.search(term = "tw", contentType = ContentType.Series)
                val allEmissions = cancelAndConsumeRemainingEvents()
                    .filterIsInstance<Event.Item<ContentSearchResults>>()
                    .map { it.value }
                assertEquals(1, allEmissions.size)
                assertEquals(ContentSearchResults.NoSearchPerformed, allEmissions.first())
            }
        }
    }

    @Test
    fun `given search call succeeds, when collecting results flow, and search term three characters or longer is entered, search results are emitted`() {
        val repository = contentSearchRepository(successfulSearchClient)

        runBlocking {
            repository.searchResults.test {
                repository.search(term = "twin", contentType = ContentType.Series)
                val allEmissions = cancelAndConsumeRemainingEvents()
                    .filterIsInstance<Event.Item<ContentSearchResults>>()
                    .map { it.value }
                assertEquals(
                    ContentSearchResults.Success(listOf(twinPeaksContentSearchResult)),
                    allEmissions.last()
                )
            }
        }
    }

    @Test
    fun `given search call fails, when collecting results flow, and search term is entered, failure state is emitted`() {
        val repository = contentSearchRepository(failingSearchClient)

        runBlocking {
            repository.searchResults.test {
                repository.search(term = "twin", contentType = ContentType.Series)
                val allEmissions = cancelAndConsumeRemainingEvents()
                    .filterIsInstance<Event.Item<ContentSearchResults>>()
                    .map { it.value }
                assertEquals(
                    ContentSearchResults.Failure,
                    allEmissions.last()
                )
            }
        }
    }

    private fun contentSearchRepository(
        searchClient: OmdbContentSearchClient
    ) = ApiContentSearchRepository(
        searchClient = searchClient,
        dispatchers = rule.testDispatchers
    )
}

private val successfulSearchClient = object : OmdbContentSearchClient {
    override suspend fun search(
        searchTerm: String,
        contentType: String,
        apiKey: String
    ) = twinPeaksApiSearchResults
}

private val failingSearchClient = object : OmdbContentSearchClient {
    override suspend fun search(
        searchTerm: String,
        contentType: String,
        apiKey: String
    ): ApiSearchResults {
        throw Throwable()
    }
}
