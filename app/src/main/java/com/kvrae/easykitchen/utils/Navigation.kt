package com.kvrae.easykitchen.utils

import SearchBarLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kvrae.easykitchen.data.remote.dto.asMealDetail
import com.kvrae.easykitchen.presentation.forget_password.ForgetPasswordScreen
import com.kvrae.easykitchen.presentation.login.LoginScreen
import com.kvrae.easykitchen.presentation.main_screen.MainScreen
import com.kvrae.easykitchen.presentation.meal_detail.MealDetailsScreen
import com.kvrae.easykitchen.presentation.meals.MealsViewModel
import com.kvrae.easykitchen.presentation.register.RegisterScreen
import com.kvrae.easykitchen.presentation.splach_screen.SplashScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


// setting the navigation composable
@Composable
fun App() {
    Navigation()
}

// App Navigation routes
const val SPLASH_SCREEN_ROUTE = "splash"
const val LOGIN_SCREEN_ROUTE = "login"
const val REGISTER_SCREEN_ROUTE = "register"
const val FORGET_PASS_SCREEN_ROUTE = "forget"
const val MAIN_SCREEN_ROUTE = "main"
const val MEAL_DETAILS_SCREEN_ROUTE = "details"
// Main screen routes
const val MAIN_HOME_ROUTE = "Home"
const val MAIN_MEALS_ROUTE = "Meals"
const val MAIN_COMPOSE_ROUTE = "Compose"
const val MAIN_CHAT_ROUTE = "Chat"


const val SEARCH_BAR_LAYOUT_ROUTE = "search"
// Forgetting password routes
const val EMAIL_FPS_ROUTE = "email"
const val OTP_FPS_ROUTE = "otp"
const val PASSWORD_FPS_ROUTE = "password"

// setting navigator class
sealed class Screen(
    val route: String,
) {
    data object SplashScreen : Screen(SPLASH_SCREEN_ROUTE)

    data object LoginScreen : Screen(LOGIN_SCREEN_ROUTE)

    data object RegisterScreen : Screen(REGISTER_SCREEN_ROUTE)

    data object ForgetPassScreen : Screen(FORGET_PASS_SCREEN_ROUTE)

    data object MainScreen : Screen(MAIN_SCREEN_ROUTE)

    data object SearchLayout : Screen(SEARCH_BAR_LAYOUT_ROUTE)

    data object MealDetailsScreen : Screen(MEAL_DETAILS_SCREEN_ROUTE)
}

// setting the navigation composable
@Composable
fun Navigation() {
    val mealsViewModel = koinViewModel<MealsViewModel>()
    val userPreferencesManager: UserPreferencesManager = koinInject()
    val isNetworkOn = rememberNetworkConnectivity()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                navController = navController,
            )
        }

        composable(Screen.ForgetPassScreen.route) {
            ForgetPasswordScreen(
                navController = navController,
            )
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(
                navController = navController,
            )
        }

        composable(Screen.RegisterScreen.route) {
            RegisterScreen(
                navController = navController,
            )
        }
        composable(Screen.MainScreen.route) {
            MainScreen(
                navController = navController,
                isNetworkOn = isNetworkOn,
                onLogout = {
                    scope.launch {
                        userPreferencesManager.clearUserData()
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.MainScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable("${Screen.MealDetailsScreen.route}/{mealId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            val meals by mealsViewModel.meals.collectAsState()

            // Ensure data is present when navigating directly
            LaunchedEffect(mealId, meals) {
                if (mealId != null && meals.isEmpty()) {
                    mealsViewModel.fetchMeals()
                }
            }

            val meal = remember(meals, mealId) { meals.firstOrNull { it.idResponse == mealId } }

            when (meal) {
                null if mealId == null -> {
                    Text("Meal not found")
                }
                null -> {
                    // Show lightweight placeholder while fetching/looking up the meal
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    MealDetailsScreen(
                        navController = navController,
                        meal = meal.asMealDetail(),
                    )
                }
            }
        }
        composable(Screen.SearchLayout.route) {
            SearchBarLayout(
                navController = navController,
                items = emptyList(),
            )
        }
    }
}


fun NavController.popThenNavigateTo(
    navigateRoute: String,
    popRoute: String,
) {
    this.navigate(navigateRoute) {
        popUpTo(popRoute) {
            inclusive = true
        }
    }
}
