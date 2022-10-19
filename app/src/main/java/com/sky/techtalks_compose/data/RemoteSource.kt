package com.sky.techtalks_compose.data

import com.sky.techtalks_compose.models.FoodItem
import com.sky.techtalks_compose.models.response.CategoriesResponse
import com.sky.techtalks_compose.models.response.MealsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteSource @Inject constructor(private val foodMenuApi: ApiClient) {

    private var cachedCategories: List<FoodItem>? = null

    suspend fun getFoodCategories(): List<FoodItem> = withContext(Dispatchers.IO) {
        var cachedCategories = cachedCategories
        if (cachedCategories == null) {
            cachedCategories = foodMenuApi.getFoodCategories().mapCategoriesToItems()
            this@RemoteSource.cachedCategories = cachedCategories
        }
        return@withContext cachedCategories
    }

    suspend fun getMealsByCategory(categoryId: String) = withContext(Dispatchers.IO) {
        val categoryName = getFoodCategories().first { it.id == categoryId }.name
        return@withContext foodMenuApi.getMealsByCategory(categoryName).mapMealsToItems()
    }

    private fun CategoriesResponse.mapCategoriesToItems(): List<FoodItem> {
        return this.categories.map { category ->
            FoodItem(
                id = category.id,
                name = category.name,
                description = category.description,
                thumbnailUrl = category.thumbnailUrl
            )
        }
    }

    private fun MealsResponse.mapMealsToItems(): List<FoodItem> {
        return this.meals.map { category ->
            FoodItem(
                id = category.id,
                name = category.name,
                thumbnailUrl = category.thumbnailUrl
            )
        }
    }
}