package com.sky.techtalks_compose.ui.feature.category_detail

import com.sky.techtalks_compose.models.FoodItem

class CategoryDetailContract {
    data class State(
        val category: FoodItem?,
        val categoryFoodItems: List<FoodItem>
    )
}