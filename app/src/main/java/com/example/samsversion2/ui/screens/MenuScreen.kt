package com.example.samsversion2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("list") }) {
            Text("Go to List Screen")
        }
        Button(onClick = { navController.navigate("calculator") }) {
            Text("Go to Calculator Screen")
        }
        Button(onClick = { navController.navigate("addItem")}) {
            Text("Add Item to List")
        }
    }
}
