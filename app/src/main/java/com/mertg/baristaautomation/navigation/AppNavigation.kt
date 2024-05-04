package com.mertg.baristaautomation.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mertg.baristaautomation.view.MainPage
import com.mertg.baristaautomation.view.MainScaffold
import com.mertg.baristaautomation.view.ShowOrdersPage
import com.mertg.baristaautomation.viewmodel.AuthViewModel

@Composable
fun AllAppNavigation(navController : NavHostController) {
    val viewModel: AuthViewModel = AuthViewModel()

    NavHost(navController, startDestination = Screen.MainPage.route) {
        composable(Screen.MainPage.route) {
            MainPage()
        }
        composable(Screen.LoginPage.route) {
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Screen.ShowOrdersPage.route){
            ShowOrdersPage()
        }
    }
}

@Composable
fun StartNavigation(navController: NavHostController) {
    val viewModel: AuthViewModel = AuthViewModel()

    NavHost(navController, startDestination = Screen.LoginPage.route) {
        composable(Screen.MainScaffold.route){
            MainScaffold()
        }
        composable(Screen.LoginPage.route){
            LoginScreen(viewModel = viewModel, navController = navController)
        }
    }
}