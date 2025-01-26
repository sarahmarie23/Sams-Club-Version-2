package com.example.samsversion2.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.samsversion2.ui.viewmodel.ListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(navController: NavHostController, viewModel: ListViewModel) {
    var itemNumber by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("Item name will appear here") }
    var imageUrl by remember { mutableStateOf("") }
    val testList = arrayOf("Default list", "Default list2", "Default list3")
    var selectedList by remember { mutableStateOf(testList[0]) }
    var expanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter item number")
        TextField(
            value = itemNumber,
            onValueChange = { itemNumber = it },
            label = { Text("Item number") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Select list")
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedList,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                testList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedList = item
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (imageUrl.isNotEmpty()) {
            Text("Image saved successfully!")
        } else {
            Text("Image will appear here")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {
                itemNumber = ""
                itemName = "Item name will appear here"
                imageUrl = ""
            }) {
                Text("Clear")
            }
            Button(onClick = {
                val url = "https://www.samsclub.com/s/$itemNumber"
                coroutineScope.launch {
                    Log.d("AddItemScreen", "LAUNCHING")
                    fetchAndSaveItemData(itemNumber, url, context, viewModel)
                }
                Toast.makeText(context, "Item added", Toast.LENGTH_SHORT).show()
            }) {
                Text("Submit")
            }
        }
    }
}

suspend fun fetchAndSaveItemData(itemNumber: String, url: String, context: Context, viewModel: ListViewModel) {
    Log.d("AddItemScreen", "HERE")
    withContext(Dispatchers.IO){

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response0 = client.newCall(request).execute()
        if (response0.isSuccessful) {
            Log.d("Network", "Successfully reached the website: ${response0.body?.string()}")
        } else {
            Log.e("Network", "Failed to reach the website. Status code: ${response0.code}")
        }

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                Log.d("AddItemScreen", "right before html")
                val html = response.body?.string()
                val doc = Jsoup.parse(html)
//                doc.select("li[style='display: none !important;'][picreplacementreplaced='true']").forEach { li ->
//                    li.empty()
//                }
//                Log.d("AddItemScreen", "doc after remove: $doc")
                val imageUrl =
                    doc.select("img.sc-pc-image-controller.sc-image-wrapper-full-res.sc-image-wrapper-full-res-loaded")
                        .attr("src")
                val itemName =
                    doc.select("img.sc-pc-image-controller.sc-image-wrapper-full-res.sc-image-wrapper-full-res-loaded")
                        .attr("alt")
                Log.d("AddItemScreen", "Image URL: $imageUrl")
                Log.d("AddItemScreen", "Item Name: $itemName")
                // Download and save the image
                val imageFile = File(context.filesDir, "image_$itemNumber.jpg")

                    val validImageUrl = if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                        "http://$imageUrl"
                    } else {
                        imageUrl
                    }
                val imageRequest = Request.Builder().url(validImageUrl).build()

                client.newCall(imageRequest).execute().use { imageResponse ->
                    val inputStream = imageResponse.body?.byteStream()
                    FileOutputStream(imageFile).use { outputStream ->
                        inputStream?.copyTo(outputStream)
                    }
                }

                viewModel.addItemToDefaultList(
                    itemNumber = itemNumber,
                    itemName = itemName,
                    imageUrl = imageFile.absolutePath,
                    isDownloaded = true
                )
            }
        } catch (e: Exception) {
            Log.d("AddItemScreen", "Error fetching item data")
            e.printStackTrace()
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    AddItemScreen(navController = rememberNavController(),)
}
 */