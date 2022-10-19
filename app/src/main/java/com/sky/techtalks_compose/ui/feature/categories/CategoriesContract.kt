package com.sky.techtalks_compose.ui.feature.categories

import com.sky.techtalks_compose.models.FoodItem

class CategoriesContract {
    data class State(
        val categories: List<FoodItem> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}