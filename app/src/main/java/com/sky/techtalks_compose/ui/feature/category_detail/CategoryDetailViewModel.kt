package com.sky.techtalks_compose.ui.feature.category_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.techtalks_compose.data.RemoteSource
import com.sky.techtalks_compose.ui.navigation.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: RemoteSource
) : ViewModel() {

    var state by mutableStateOf(CategoryDetailContract.State(null, listOf()))
        private set

    init {
        viewModelScope.launch {
            //get categoryId from navGraph
            val categoryId = stateHandle.get<String>(NavigationKeys.Arg.CATEGORY_ID)
                ?: throw IllegalStateException("No categoryId was passed to destination.")

            getFoodCategory(categoryId)
            getFoodItemsByCategory(categoryId)

        }
    }

    private suspend fun getFoodCategory(categoryId: String) {
        val category = repository.getFoodCategories().first { it.id == categoryId }
        state = state.copy(category = category)
    }

    private suspend fun getFoodItemsByCategory(categoryId: String) {
        val foodItems = repository.getMealsByCategory(categoryId)
        state = state.copy(categoryFoodItems = foodItems)
    }
}