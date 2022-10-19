package com.sky.techtalks_compose.ui.feature.categories

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.sky.techtalks_compose.models.FoodItem
import com.sky.techtalks_compose.ui.navigation.NavigationKeys
import com.sky.techtalks_compose.ui.noRippleClickable
import com.sky.techtalks_compose.ui.theme.TechTalksComposeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CategoriesView(state: CategoriesContract.State, effectFlow: Flow<CategoriesContract.Effect>?, onNavigationRequested: (itemId: String) -> Unit){
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    //listen to side effects from viewModel
    LaunchedEffect(effectFlow, block = {
        effectFlow?.onEach { effect ->
            if(effect is CategoriesContract.Effect.DataWasLoaded){
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Food is loaded",
                    duration = SnackbarDuration.Short
                )
            }
        }?.collect()
    })

    Scaffold(scaffoldState = scaffoldState, topBar = {
        CategoriesAppBar()
    }) {
        it.calculateTopPadding()
        Box {
            CategoriesList(state.categories){itemId ->
                onNavigationRequested(itemId)
            }
            if (state.isLoading)
                LoadingBar()
        }
    }
}

@Composable
fun CategoriesList(
    foodItems: List<FoodItem>,
    onItemClicked: (id: String) -> Unit = { }
) {
    LazyColumn(
        Modifier.background(Color.LightGray),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        items(foodItems) { item ->
            CategoryItemRow(item = item, itemShouldExpand = true, onItemClicked = onItemClicked)
        }
    }
}

@Preview(name="itemRow", showBackground = true)
@Composable
fun PreviewItemRow() = CategoryItemRow(item = FoodItem("2", "Food Name", "https://logos.fandom.com/wiki/SkyShowtime?file=SkyShowtime_app_icon.png"))

@Composable
fun CategoryItemRow(
    item: FoodItem,
    itemShouldExpand: Boolean = false,
    iconTransformationBuilder: ImageRequest.Builder.() -> Unit = { },
    onItemClicked: (id: String) -> Unit = { }){

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onItemClicked(item.id) }
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        Row(modifier = Modifier.animateContentSize()) {
            Box(modifier = Modifier.align(alignment = Alignment.Top)) {
                FoodItemThumbnail(item.thumbnailUrl, iconTransformationBuilder)
            }
            FoodItemDetails(
                item = item,
                expandedLines = if (expanded) 10 else 2,
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 24.dp,
                        bottom = 24.dp
                    )
                    .fillMaxWidth(0.80f)
                    .align(Alignment.CenterVertically)
            )
            if (itemShouldExpand)
                Box(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .noRippleClickable { expanded = !expanded }
                ) {
                    ExpandableContentIcon(expanded)
                }
        }
    }
}

@Composable
fun FoodItemDetails(
    item: FoodItem?,
    expandedLines: Int,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = item?.name ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        if (item?.description?.trim()?.isNotEmpty() == true)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = item.description.trim(),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    maxLines = expandedLines
                )
            }
    }
}

@Composable
fun FoodItemThumbnail(
    thumbnailUrl: String,
    iconTransformationBuilder: ImageRequest.Builder.() -> Unit
) {
    Image(
        painter = rememberImagePainter(
            data = thumbnailUrl,
            builder = iconTransformationBuilder
        ),
        modifier = Modifier
            .size(88.dp)
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        contentDescription = "Food item thumbnail picture",
    )
}

@Composable
private fun ExpandableContentIcon(expanded: Boolean) {
    val navController = rememberNavController()
    IconButton(onClick = {
        navController.navigateUp()
    }) {
        Icon(
            imageVector = if (expanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown,
            contentDescription = "Expand row icon",
            modifier = Modifier
                .padding(all = 16.dp)
        )
    }
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun CategoriesAppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Home,
                modifier = Modifier.padding(horizontal = 12.dp),
                contentDescription = "Action icon"
            )
        },
        title = { Text(stringResource(com.sky.techtalks_compose.R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview(){
    TechTalksComposeTheme(darkTheme = true) {
        CategoriesView(CategoriesContract.State(), null) { }
    }
}