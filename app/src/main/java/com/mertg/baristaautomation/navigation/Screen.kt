package com.mertg.baristaautomation.navigation

sealed class Screen(val route : String){
    data object MainPage : Screen(route = "main_page_route")
    data object LoginPage : Screen(route = "login_route")
    data object ShowOrdersPage : Screen(route = "show_orders_route")
    data object MainScaffold : Screen(route = "main_scaffold_route")
/*    data object DetailPage : Screen(route = "route_detail_page/{item_name}"){
        fun passItemName(item : String) : String{
            return "route_detail_page/${item}"
        }
    }*/
}