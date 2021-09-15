package com.joetimmins.watchbox.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joetimmins.watchbox.model.*
import com.joetimmins.watchbox.model.http.OmdbService
import com.joetimmins.watchbox.util.InjectableDispatchers
import com.joetimmins.watchbox.util.RequestStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val repository: ContentSearchRepository,
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(SearchUiState())
    val uiStateFlow = _uiStateFlow as StateFlow<SearchUiState>

    init {
        viewModelScope.launch {
            repository.searchResults.collect {
                val uiState = _uiStateFlow.value
                val newUiState = uiState.updateWith(it)
                _uiStateFlow.tryEmit(newUiState)
            }
        }
    }

    fun onSearchTermChanged(term: String) {
        _uiStateFlow.update { uiState ->
            uiState.copy(searchTerm = term)
        }
        if (term.length > 2) {
            val uiState = _uiStateFlow.value
            _uiStateFlow.tryEmit(
                uiState.copy(requestStatus = RequestStatus.Requesting)
            )
            repository.search(term = term, contentType = uiState.selectedContentType)
        }
    }

    fun onMoviesSelected() {
        _uiStateFlow.update { uiState ->
            uiState.copy(selectedContentType = ContentType.Movie)
        }
        repository.search(_uiStateFlow.value.searchTerm, ContentType.Movie)
    }

    fun onSeriesSelected() {
        _uiStateFlow.update { uiState ->
            uiState.copy(selectedContentType = ContentType.Series)
        }
        repository.search(_uiStateFlow.value.searchTerm, ContentType.Series)
    }
}

private fun List<ContentSearchResult>.toCardData() = map { it.toCardData() }

private fun ContentSearchResult.toCardData() = ContentCardData(
    title = title,
    posterUrl = posterUrl
)

private fun SearchUiState.updateWith(results: ContentSearchResults) = when (results) {
    ContentSearchResults.Failure -> copy(requestStatus = RequestStatus.Failure)
    ContentSearchResults.NoSearchPerformed -> copy(requestStatus = RequestStatus.NotSent)
    is ContentSearchResults.Success -> copy(
        requestStatus = RequestStatus.Success,
        allCardData = results.results.toCardData()
    )
}

data class SearchUiState(
    val requestStatus: RequestStatus = RequestStatus.NotSent,
    val selectedContentType: ContentType = ContentType.Movie,
    val searchTerm: String = "",
    val allCardData: List<ContentCardData> = emptyList()
)

class SearchViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dispatchers = InjectableDispatchers()
        val repository = ApiContentSearchRepository(OmdbService.client, dispatchers)
        val viewModel = SearchViewModel(repository)
        return modelClass.cast(viewModel)!!
    }
}