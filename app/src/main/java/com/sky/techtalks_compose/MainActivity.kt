package com.sky.techtalks_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sky.techtalks_compose.ui.feature.categories.CategoriesView
import com.sky.techtalks_compose.ui.feature.categories.CategoriesViewModel
import com.sky.techtalks_compose.ui.feature.category_detail.CategoryDetailView
import com.sky.techtalks_compose.ui.feature.category_detail.CategoryDetailViewModel
import com.sky.techtalks_compose.ui.navigation.NavigationKeys
import com.sky.techtalks_compose.ui.theme.TechTalksComposeTheme
import com.sky.techtalks_compose.ui.theme.black60
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechTalksComposeTheme() {
                FoodApp()
            }
        }
    }
}

@Composable
fun FoodApp() {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    if (shouldShowOnboarding) {
        OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
    } else {
        SetupNavigation()
    }
}

@Composable
fun SetupNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationKeys.Route.CATEGORIES_LIST
    ) {
        composable(route = NavigationKeys.Route.CATEGORIES_LIST) {
            CategoriesDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.CATEGORY_DETAIL,
            arguments = listOf(navArgument(NavigationKeys.Arg.CATEGORY_ID) {
                type = NavType.StringType
            })
        ) {
            CategoryDetailDestination()
        }
    }
}

@Composable
private fun CategoriesDestination(navController: NavHostController) {
    val viewModel: CategoriesViewModel = hiltViewModel()
    CategoriesView(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.CATEGORIES_LIST}/${itemId}")
        })
}

@Composable
private fun CategoryDetailDestination() {
    val viewModel: CategoryDetailViewModel = hiltViewModel()
    CategoryDetailView(viewModel.state)
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit) {
    Surface() {
        Image(
            painter = painterResource(id = R.drawable.meal_pizza_background),
            contentDescription = "background meal",
            contentScale = ContentScale.Crop
        )

        Surface(color = black60, modifier = Modifier.fillMaxSize(), content = { })

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Compose your meal",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Start", fontSize = 20.sp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 480, heightDp = 800)
@Composable
fun OnboardingPreview() {
    TechTalksComposeTheme() {
        OnboardingScreen(onContinueClicked = {})
    }
}