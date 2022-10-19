package com.sky.techtalks_compose.models.response

import com.google.gson.annotations.SerializedName

data class MealsResponse(val meals: List<MealResponse>)
data class MealResponse(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val thumbnailUrl: String
)