package dev.vengateshm.dictionaryapp.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vengateshm.dictionaryapp.core.util.Resource
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.use_case.GetWordInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _wordInfoState = mutableStateOf(WordInfoState())
    val wordInfoState: State<WordInfoState> = _wordInfoState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var _searchJob: Job? = null

    fun onSearch(query: String) {
        _searchQuery.value = query
        _searchJob?.cancel()
        _searchJob = viewModelScope.launch {
            delay(500L)
            getWordInfo(query)
                .onEach { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _wordInfoState.value = wordInfoState.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                        is Resource.Success -> {
                            _wordInfoState.value = wordInfoState.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _wordInfoState.value = wordInfoState.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _uiEvent.emit(UiEvent.ShowSnackBar(result.message ?: "Unknown Error"))
                        }
                    }
                }.launchIn(this)
        }
    }

    // One time events
    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
    }
}