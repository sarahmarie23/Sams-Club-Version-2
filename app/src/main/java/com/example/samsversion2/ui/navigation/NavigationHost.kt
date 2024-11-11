package com.example.samsversion2.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.samsversion2.ui.screens.AddItemScreen
import com.example.samsversion2.ui.viewmodel.ListViewModel
import com.example.samsversion2.ui.screens.ListScreen
import com.example.samsversion2.ui.screens.MenuScreen
import com.example.samsversion2.ui.screens.RenewalEstimatorScreen


@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(navController)
        }
        composable("list") {
            val listViewModel: ListViewModel = hiltViewModel()
            ListScreen(navController, listViewModel)
        }
        composable("calculator") {
            RenewalEstimatorScreen(navController)
        }
        composable("addItem") {
            AddItemScreen(navController)
        }
    }
}
