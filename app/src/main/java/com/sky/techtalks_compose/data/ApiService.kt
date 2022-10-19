package com.sky.techtalks_compose.data

import com.sky.techtalks_compose.models.response.CategoriesResponse
import com.sky.techtalks_compose.models.response.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("categories.php")
    suspend fun getFoodCategories(): CategoriesResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") categoryId: String): MealsResponse
}

