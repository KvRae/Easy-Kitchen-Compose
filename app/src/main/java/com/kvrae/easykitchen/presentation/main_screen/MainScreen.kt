package com.kvrae.easykitchen.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.kvrae.easykitchen.presentation.miscellaneous.components.TopBar
import com.kvrae.easykitchen.utils.MAIN_CHAT_ROUTE
import com.kvrae.easykitchen.utils.MAIN_COMPOSE_ROUTE
import com.kvrae.easykitchen.utils.MAIN_MEALS_ROUTE
import com.kvrae.easykitchen.utils.navItems
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavController,
    isNetworkOn: Boolean,
) {
    var navItem by rememberSaveable {
        mutableStateOf(navItems.first().name)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                ModalDrawerSheetContent()
            }
        },
    ) {
        MainScreenScaffold(
            isNetworkOn = isNetworkOn,
            navController = navController,
            navItem = navItem,
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
    navItem: String,
    onNavItemChange: (String) -> Unit,
    onMenuClick: () -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val internetConnectionError = stringResource(id = R.string.no_internet_connection)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = navItem,
                onActionClick = onMenuClick,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = { paddingValues ->
            paddingValues
            MainScreenNavigation(
                modifier =
                    Modifier
                        .navigationBarsPadding()
                        .statusBarsPadding()
                        .padding(vertical = 64.dp),
                navItem = navItem,
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
        bottomBar = {
            BottomNavBar(
                navItems = navItems,
                navItem = navItem,
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

@Composable
fun ModalDrawerSheetContent(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .padding(16.dp)
                .fillMaxWidth(.6f),
    ) {
        Text(text = "Welcome to EasyKitchen")
        ListItem(
            headlineContent = { /*TODO*/ },
            trailingContent = { /*TODO*/ },
        )
    }
}
