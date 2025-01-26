package com.example.samsversion2.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.samsversion2.data.model.Item
import com.example.samsversion2.ui.viewmodel.ListViewModel

@Composable
fun ListScreen(navController: NavHostController, viewModel: ListViewModel) {
    val items by viewModel.defaultListItems.observeAsState(emptyList())
    Log.d("ListScreen", "listscreen is being triggered")

    ItemList(items = items)
}

@Composable
fun ItemRow(item: Item) {
    Log.d("ListScreen", "itemrow is being triggered")
    val imagePath = item.imagePath
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = if (item.isDownloaded) rememberAsyncImagePainter(imagePath) else painterResource(id = imagePath.toInt()),
            //painter = painterResource(id = item.imagePath),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = item.itemName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Item #: ${item.itemNumber}", fontSize = 14.sp)
        }
    }
}

@Composable
fun ItemList(items: List<Item>, modifier: Modifier = Modifier) {
    Log.d("listscreen", "itemlist is being triggered and size is ${items.size}")

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            Log.d("listscreen", "${item.itemName} is being triggered")

            ItemRow(item)
        }
    }
}