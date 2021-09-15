package com.joetimmins.watchbox.model

import com.joetimmins.watchbox.model.http.OmdbContentSearchClient
import com.joetimmins.watchbox.util.InjectableDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class ContentSearchRepository(
    private val searchClient: OmdbContentSearchClient,
    private val dispatchers: InjectableDispatchers
) {
    private val searchInput =
        MutableStateFlow(SearchInput(term = "", contentType = ContentType.Movie))

    val searchResults: Flow<ContentSearchResults> = searchInput
        .filter { it.term.length > 2 }
        .mapLatest { searchFor(it) }
        .map { apiSearchResults -> apiSearchResults.toContentSearchResults() }
        .onStart { emit(ContentSearchResults.NoSearchPerformed) }

    private suspend fun searchFor(input: SearchInput) = withContext(dispatchers.io) {
        runCatching {
            val search = searchClient.search(
                searchTerm = input.term,
                contentType = input.contentType.rawContentType
            )
            search
        }
    }

    fun search(term: String, contentType: ContentType) {
        searchInput.tryEmit(SearchInput(term, contentType))
    }
}

private data class SearchInput(
    val term: String,
    val contentType: ContentType
)

sealed interface ContentSearchResults {
    object NoSearchPerformed : ContentSearchResults
    object Failure : ContentSearchResults
    data class Success(val results: List<ContentSearchResult>) : ContentSearchResults
}

data class ContentSearchResult(
    val title: String,
    val imdbId: String,
    val contentType: ContentType,
    val airDate: AirDate,
    val posterUrl: String,
)

private fun Result<ApiSearchResults>.toContentSearchResults(): ContentSearchResults =
    map { it.toSuccessState() }.getOrDefault(ContentSearchResults.Failure)

private fun ApiSearchResults.toSuccessState() = ContentSearchResults.Success(
    results.map {
        it.toContentSearchResult()
    }
)

private fun ApiSearchResult.toContentSearchResult() = ContentSearchResult(
    title = title,
    imdbId = imdbId,
    posterUrl = posterUrl,
    contentType = ContentType.fromRawValue(contentType),
    airDate = AirDate.fromYearRange(yearRange)
)
