package com.kvrae.easykitchen.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.presentation.chat.ChatScreen
import com.kvrae.easykitchen.presentation.home.HomeScreen
import com.kvrae.easykitchen.presentation.ingrendient.IngredientsScreen
import com.kvrae.easykitchen.presentation.meals.MealsScreen
import com.kvrae.easykitchen.presentation.miscellaneous.components.BottomNavBar
import com.kvrae.easykitchen.presentation.miscellaneous.components.ModalDrawerSheetContent
import com.kvrae.easykitchen.presentation.miscellaneous.components.TopBar
import com.kvrae.easykitchen.utils.MAIN_CHAT_ROUTE
import com.kvrae.easykitchen.utils.MAIN_COMPOSE_ROUTE
import com.kvrae.easykitchen.utils.MAIN_MEALS_ROUTE
import com.kvrae.easykitchen.utils.getNavItemByName
import com.kvrae.easykitchen.utils.navItems
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavController,
    isNetworkOn: Boolean,
    onLogout: () -> Unit
) {
    var navItem by rememberSaveable {
        mutableStateOf(navItems.first().name)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                ModalDrawerSheetContent(
                    onItemClick = { route ->
                        navItem = route
                        scope.launch { drawerState.close() }
                    },
                    selectedRoute = navItem,
                    onLogout = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        },
    ) {
        MainScreenScaffold(
            isNetworkOn = isNetworkOn,
            navController = navController,
            selectedRoute = navItem,
            onNavItemChange = {
                navItem = it
            },
            onMenuClick = {
                scope.launch {
                    Log.d("MainScreen Click", "Menu Clicked")
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            },
        )
    }
}

@Composable
fun MainScreenScaffold(
    modifier: Modifier = Modifier,
    isNetworkOn: Boolean,
    navController: NavController,
    selectedRoute: String,
    onNavItemChange: (String) -> Unit,
    onMenuClick: () -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val internetConnectionError = stringResource(id = R.string.no_internet_connection)
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = { paddingValues ->
            MainScreenNavigation(
                modifier = Modifier.padding(paddingValues),
                navItem = selectedRoute,
                navController = navController,
            )
            DisposableEffect(key1 = isNetworkOn) {

                if (!isNetworkOn) {
                    scope.launch {
                        snackBarHostState
                            .showSnackbar(
                                message = internetConnectionError,
                                duration = SnackbarDuration.Indefinite,
                            )
                    }
                }
                onDispose {
                    scope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }
        },
        topBar = {
            TopBar(
                onActionClick = onMenuClick,
                title = getNavItemByName(selectedRoute)?.title,
                description = getNavItemByName(selectedRoute)?.description,
                name = selectedRoute,
            )
        },
        bottomBar = {
            BottomNavBar(
                navItems = navItems,
                navItem = selectedRoute,
                onNavItemSelect = {
                    onNavItemChange(it)
                },
            )
        },
    )
}

@Composable
fun MainScreenNavigation(
    modifier: Modifier,
    navItem: String? = null,
    navController: NavController,
) {
    when (navItem) {
        MAIN_MEALS_ROUTE ->
            MealsScreen(
                modifier = modifier,
                navController = navController,
            )
        MAIN_COMPOSE_ROUTE ->
            IngredientsScreen(
                modifier = modifier,
            )
        MAIN_CHAT_ROUTE -> {
            ChatScreen(
                modifier = modifier
            )
        }
        else ->
            HomeScreen(
                modifier = modifier,
                navController = navController,
            )
    }
}
