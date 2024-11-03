package com.example.samsversion2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.samsversion2.ui.ItemViewModel
import com.example.samsversion2.ui.ListScreen

class MainActivity : ComponentActivity() {
    private val viewModel: ItemViewModel by viewModels {
        ItemViewModel.provideFactory((application as SamsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListScreen(viewModel)
        }
    }
}

