package com.sky.techtalks_compose.ui.feature.category_detail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.sky.techtalks_compose.models.FoodItem
import com.sky.techtalks_compose.ui.feature.categories.CategoryItemRow
import com.sky.techtalks_compose.ui.feature.categories.FoodItemDetails
import java.lang.Float.min
import kotlin.math.max

@Preview
@Composable
fun PreviewCategoryDetail() = CategoryDetailView(state = CategoryDetailContract.State(FoodItem("2", "FoodName", "www.google.com"), listOf()))

@Composable
fun CategoryDetailView(state: CategoryDetailContract.State){
    val scrollState = rememberLazyListState()
    val scrollOffState = min(1f, 1-(scrollState.firstVisibleItemScrollOffset / 600f+scrollState.firstVisibleItemIndex))

    Surface(color = MaterialTheme.colors.background) {
        Column {
            Surface(elevation = 4.dp) {
                CategoryDetailToolbar(state.category, scrollOffState)
            }
            Spacer(modifier = Modifier.height(2.dp))
            LazyColumn(state = scrollState, contentPadding = PaddingValues(bottom = 16.dp)){
                items(state.categoryFoodItems) { item ->
                    CategoryItemRow(
                        item = item,
                        iconTransformationBuilder = {
                            transformations(
                                CircleCropTransformation()
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryDetailToolbar(
    category: FoodItem?,
    scrollOffset: Float,
) {
    val imageSize by animateDpAsState(targetValue = max(72.dp, 128.dp * scrollOffset))
    Row {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = Color.Black
            ),
            elevation = 4.dp
        ) {
            Image(
                painter = rememberImagePainter(
                    data = category?.thumbnailUrl,
                    builder = {
                        transformations(CircleCropTransformation())
                    },
                ),
                modifier = Modifier.size(max(72.dp, imageSize)),
                contentDescription = "Food category thumbnail picture",
            )
        }
        FoodItemDetails(
            item = category,
            expandedLines = (max(3f, scrollOffset * 6)).toInt(),
            modifier = Modifier
                .padding(
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth()
        )
    }
}