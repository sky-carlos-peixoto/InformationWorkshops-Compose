package com.sky.techtalks_compose.data

import com.sky.techtalks_compose.models.response.CategoriesResponse
import com.sky.techtalks_compose.models.response.MealsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor(private val service: ApiService) {

    suspend fun getFoodCategories(): CategoriesResponse = service.getFoodCategories()
    suspend fun getMealsByCategory(categoryId: String): MealsResponse =
        service.getMealsByCategory(categoryId)

    companion object {
        const val API_URL = "https://www.themealdb.com/api/json/v1/1/"
    }
}


