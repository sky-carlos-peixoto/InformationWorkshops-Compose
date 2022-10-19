package com.sky.techtalks_compose.ui.feature.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.techtalks_compose.data.RemoteSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val remoteSource: RemoteSource) :
    ViewModel() {

    var state by mutableStateOf(
        CategoriesContract.State(
            categories = listOf(),
            isLoading = true
        )
    )
        private set

    var effects = Channel<CategoriesContract.Effect>(Channel.UNLIMITED)
        private set

    init {
        viewModelScope.launch { getFoodCategories() }
    }

    private suspend fun getFoodCategories() {
        val categories = remoteSource.getFoodCategories()
        viewModelScope.launch {
            state = state.copy(categories = categories, isLoading = false)
            effects.send(CategoriesContract.Effect.DataWasLoaded)
        }
    }
}
