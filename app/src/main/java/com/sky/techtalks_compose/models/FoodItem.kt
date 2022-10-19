package com.sky.techtalks_compose.models

data class FoodItem(
    val id: String,
    val name: String,
    val thumbnailUrl: String,
    val description: String = ""
)
