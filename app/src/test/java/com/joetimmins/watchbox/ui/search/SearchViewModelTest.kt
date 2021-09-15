package com.joetimmins.watchbox.ui.search

import app.cash.turbine.Event
import app.cash.turbine.test
import com.joetimmins.watchbox.CoroutineTestRule
import com.joetimmins.watchbox.model.*
import com.joetimmins.watchbox.util.RequestStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

private const val twinPeaksSearchTerm = "twin peaks"

@OptIn(ExperimentalTime::class)
class SearchViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @Test
    fun `when user search, ui state contains search term`() {
        val viewModel = searchViewModel(repositoryWithResults)

        runBlocking {
            viewModel.uiStateFlow.test {
                viewModel.onSearchTermChanged(twinPeaksSearchTerm)
                val allEmissions = cancelAndConsumeRemainingEvents()
                    .filterIsInstance<Event.Item<SearchUiState>>()
                    .map { it.value }
                assertEquals(
                    twinPeaksSearchTerm,
                    allEmissions.last().searchTerm
                )
            }
        }
    }

    @Test
    fun `given repository will return results, when user searches, ui state contains search results`() {
        val viewModel = searchViewModel(repositoryWithResults)

        runBlocking {
            viewModel.uiStateFlow.test {
                viewModel.onSearchTermChanged(twinPeaksSearchTerm)
                val allEmissions = cancelAndConsumeRemainingEvents()
                    .filterIsInstance<Event.Item<SearchUiState>>()
                    .map { it.value }
                assertEquals(
                    listOf(
                        ContentCardData(
                            title = "Twin Peaks",
                            posterUrl = "https://m.media-amazon.com/images/M/MV5BMTExNzk2NjcxNTNeQTJeQWpwZ15BbWU4MDcxOTczOTIx._V1_SX300.jpg",
                        ),
                        ContentCardData(
                            title = "Twin Peaks 1992",
                            posterUrl = "https://m.media-amazon.com/images/M/MV5BMTExNzk2NjcxNTNeQTJeQWpwZ15BbWU4MDcxOTczOTIx._V1_SX300.jpg",
                        )
                    ),
                    allEmissions.last().allCardData
                )
            }
        }
    }

    @Test
    fun `given repository will fail, when user searches, ui state contains failure state`() {
        val viewModel = searchViewModel(repositoryWithFailedSearch)

        runBlocking {
            viewModel.uiStateFlow.test {
                viewModel.onSearchTermChanged(twinPeaksSearchTerm)
                val allEmissions = cancelAndConsumeRemainingEvents()
                    .filterIsInstance<Event.Item<SearchUiState>>()
                    .map { it.value }
                assertEquals(
                    RequestStatus.Failure,
                    allEmissions.last().requestStatus
                )
            }
        }
    }

    @Test
    fun `when user selects different content type, ui state reflects the selection`() {
        val viewModel = searchViewModel(repositoryWithFailedSearch)

        runBlocking {
            viewModel.uiStateFlow.test {
                viewModel.onSeriesSelected()
                val allEmissions = cancelAndConsumeRemainingEvents()
                    .filterIsInstance<Event.Item<SearchUiState>>()
                    .map { it.value }
                assertEquals(
                    ContentType.Series,
                    allEmissions.last().selectedContentType
                )
            }
        }
    }

    private fun searchViewModel(repository: ContentSearchRepository) = SearchViewModel(
        repository = repository
    )
}

private val successfulSearchResults = ContentSearchResults.Success(
    results = listOf(
        twinPeaksContentSearchResult,
        twinPeaks1992ContentSearchResult,
    )
)

private val repositoryWithResults = DummyContentSearchRepository(successfulSearchResults)
private val repositoryWithFailedSearch = DummyContentSearchRepository(ContentSearchResults.Failure)

private class DummyContentSearchRepository(
    private val results: ContentSearchResults,
) : ContentSearchRepository {
    private val _searchResults = MutableStateFlow<ContentSearchResults>(
        ContentSearchResults.NoSearchPerformed
    )
    override val searchResults: Flow<ContentSearchResults> = _searchResults

    override fun search(term: String, contentType: ContentType) {
        _searchResults.tryEmit(results)
    }
}