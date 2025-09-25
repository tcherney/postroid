package com.tcherney.postroid

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tcherney.postroid.ui.theme.PostroidTheme


enum class RequestContent(val value: String) {
    HEADERS("Headers"),
    BODY("Body"),
    PARAMS("Params"),
}

@Composable
fun Home(
    modifier: Modifier = Modifier, navController: NavHostController? = null
) {
    PostroidTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                //TODO figure out how we want these to look
                //TODO add endpoint adding
                val userAPICollections: ArrayList<UserAPICollection> = arrayListOf(UserAPICollection(
                    userAPIs = listOf(UserAPI(
                        endPoint = "https://world.openfoodfacts.net/api/v2/product/788434115681",
                    ))
                ))
                val selectedCollectionIndex = remember{mutableIntStateOf(0)}
                val collectionName = remember {mutableStateOf(userAPICollections[selectedCollectionIndex.intValue].collectionName)}
                val collectionNames = remember{mutableStateListOf(userAPICollections[selectedCollectionIndex.intValue].collectionName, "New")}
                //TODO all dropdowns will need on clicks to modify the collections when user updates data
                Row {
                    TextfieldDropdownMenu(collectionName, selectedCollectionIndex, collectionNames, "Collection", 16.dp, Modifier.weight(1f))
                }
                val selectedEndpointIndex = remember{mutableIntStateOf(0)}
                val endPoint = remember {mutableStateOf(userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint)}
                val endpointNames = remember{mutableStateListOf(userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint, "New")}
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    val selectedRequestType = remember {mutableIntStateOf(0)}
                    LabeledDropdownMenu(
                        listOf("Get", "Post", "Delete", "Put"),
                        modifier = Modifier.padding(16.dp).width(75.dp),
                        selectedIndex = selectedRequestType,
                        onClick = {
                            when (selectedRequestType.intValue) {
                                0 -> {
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].requestType =
                                        RequestType.GET
                                }
                                1 -> {
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].requestType =
                                        RequestType.POST
                                }
                                2 -> {
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].requestType =
                                        RequestType.DELETE
                                }
                                3 -> {
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].requestType =
                                        RequestType.PUT
                                }
                            }
                        }
                    )
                    TextfieldDropdownMenu(endPoint, selectedEndpointIndex, endpointNames, "Endpoint", 16.dp, Modifier.weight(1f))
                }
                val selectedContentIndex = remember { mutableIntStateOf(0) }
                val contentTitles = listOf(RequestContent.HEADERS.value, RequestContent.BODY.value, RequestContent.PARAMS.value)

                LabeledBox(label = {
                        LabeledDropdownMenu(contentTitles,modifier = Modifier.background(MaterialTheme.colorScheme.primary).padding(16.dp).fillMaxWidth(), selectedContentIndex)

                }) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(1f, false)
                    ) {
                        val newHeaderKey = remember { mutableStateOf("") }
                        val newHeaderValue = remember { mutableStateOf("") }
                        val headers = remember { mutableStateListOf<Pair<String,String>>()}
                        val newParamKey = remember { mutableStateOf("") }
                        val newParamValue = remember { mutableStateOf("") }
                        val params = remember { mutableStateListOf<Pair<String,String>>()}
                        val bodyContent = remember {mutableStateOf("")}

                        if (contentTitles[selectedContentIndex.intValue] == RequestContent.HEADERS.value) {
                            headers.forEach {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(it.first, color = Color.LightGray)
                                    Text(it.second)
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    value = newHeaderKey.value,
                                    onValueChange = { newHeaderKey.value = it },
                                    modifier = Modifier.weight(1f),
                                    placeholder = { Text("Key") }
                                )
                                TextField(
                                    value = newHeaderValue.value,
                                    onValueChange = { newHeaderValue.value = it },
                                    modifier = Modifier.weight(1f),
                                    placeholder = { Text("Value") }
                                )
                                IconButton(onClick = {
                                    headers.add(Pair(newHeaderKey.value, newHeaderValue.value))
                                    newHeaderKey.value = ""
                                    newHeaderValue.value = ""
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "More options")
                                }
                            }
                        }
                        else if (contentTitles[selectedContentIndex.intValue] == RequestContent.BODY.value) {
                            Column {
                                TextField(
                                    value = bodyContent.value,
                                    onValueChange = { bodyContent.value = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("Content") }
                                )
                            }
                        }
                        else if (contentTitles[selectedContentIndex.intValue] == RequestContent.PARAMS.value) {
                            params.forEach {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(it.first, color = Color.LightGray)
                                    Text(it.second)
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    value = newParamKey.value,
                                    onValueChange = { newParamKey.value = it },
                                    modifier = Modifier.weight(1f),
                                    placeholder = { Text("Key") }
                                )
                                TextField(
                                    value = newParamValue.value,
                                    onValueChange = { newParamValue.value = it },
                                    modifier = Modifier.weight(1f),
                                    placeholder = { Text("Value") }
                                )
                                IconButton(onClick = {
                                    params.add(Pair(newParamKey.value, newParamValue.value))
                                    newParamKey.value = ""
                                    newParamValue.value = ""
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "More options")
                                }
                            }
                        }
                    }
                }
                val resonseResult = remember {mutableStateOf("")}
                IconButton(onClick = {
                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].execute {
                        Log.d("Home", it.toString())
                        resonseResult.value = it.toString() + "\n" + it.body.string()
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Run")
                }
                if (resonseResult.value.isNotEmpty()) {
                    LabeledBox(label = {
                        Text(text = "Response",
                            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                                .padding(16.dp).fillMaxWidth()
                        )

                    }) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .weight(1f, false)
                        ) {
                            Text(resonseResult.value)
                        }
                    }
                }
            }
        }
    }
}