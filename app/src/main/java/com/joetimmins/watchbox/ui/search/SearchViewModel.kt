package com.joetimmins.watchbox.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joetimmins.watchbox.ContentCardData
import com.joetimmins.watchbox.model.ContentSearchRepository
import com.joetimmins.watchbox.model.ContentType
import com.joetimmins.watchbox.model.http.OmdbService
import com.joetimmins.watchbox.util.InjectableDispatchers
import com.joetimmins.watchbox.util.RequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel(
    private val repository: ContentSearchRepository,
    private val dispatchers: InjectableDispatchers,
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(SearchUiState())
    val uiStateFlow = _uiStateFlow as StateFlow<SearchUiState>

    fun onSearchTermChanged(term: String) {
        val uiState = _uiStateFlow.value
        _uiStateFlow.tryEmit(uiState.copy(requestStatus = RequestStatus.Requesting))
    }

    fun onMovieSelected() {}

    fun onSeriesSelected() {}
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
        val repository = ContentSearchRepository(OmdbService.client, dispatchers)
        val viewModel = SearchViewModel(repository, dispatchers)
        return modelClass.cast(viewModel)!!
    }
}