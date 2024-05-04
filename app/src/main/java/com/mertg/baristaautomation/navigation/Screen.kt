package com.mertg.baristaautomation.navigation

import androidx.navigation.NavHostController

sealed class Screen(val route : String){
    data object MainPage : Screen(route = "main_page_route")
    data object LoginPage : Screen(route = "login_route")
    data object ShowOrdersPage : Screen(route = "show_orders_route")
    data object MainScaffold : Screen(route = "main_scaffold_route")
    data object ShowOrdersPageA : Screen(route = "show_orders_route/{item_name}"){
        fun passNavController(item : NavHostController) : String{
            return "show_orders_route/${item}"
        }
    }
}