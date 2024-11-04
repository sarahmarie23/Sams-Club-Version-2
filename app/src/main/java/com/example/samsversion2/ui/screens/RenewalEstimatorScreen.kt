package com.example.samsversion2.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.samsversion2.data.CalculatorPreferences
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Calendar


@Composable
fun RenewalEstimatorScreen(navController: NavHostController) {
    val context = LocalContext.current
    var costEntered by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val conversionRate = 0.15068

    val lastCostFlow = CalculatorPreferences.getLastCost(context)
    val lastCost by lastCostFlow.collectAsState(initial = null)
    LaunchedEffect(lastCost) {
        lastCost?.let { costEntered = it.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = costEntered,
            onValueChange = { costEntered = it },
            label = { Text("Enter Cost") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val cost = costEntered.toDoubleOrNull()
                if (cost != null && cost in 1.0..55.0) {
                    val convertedDays = (cost / conversionRate).toInt()
                    val calendar = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_MONTH, convertedDays)
                    }
                    result = DateFormat.getDateInstance().format(calendar.time)

                    // Save last entered cost
                    coroutineScope.launch {
                        CalculatorPreferences.saveLastCost(context, cost)
                    }
                } else {
                    result = "Invalid"
                }
            }
        ) {
            Text("Convert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                costEntered = ""
                result = ""
                coroutineScope.launch {
                    CalculatorPreferences.saveLastCost(context, 0.0)
                }
            }
        ) {
            Text("Clear")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Result: $result", style = MaterialTheme.typography.bodyLarge)
    }
}
@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {

    RenewalEstimatorScreen(navController = rememberNavController())
}