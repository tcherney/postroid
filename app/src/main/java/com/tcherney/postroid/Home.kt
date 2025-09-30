package com.tcherney.postroid

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tcherney.postroid.ui.theme.PostroidTheme


enum class RequestContent(val value: String) {
    HEADERS("Headers"),
    BODY("Body"),
    PARAMS("Params"),
}

@Composable
fun Home(userAPIViewModel: UserAPIViewModel,
    modifier: Modifier = Modifier, navController: NavHostController? = null
) {
    PostroidTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                //TODO figure out how we want these to look
                //TODO replace with dao call
                val userAPICollections = remember { mutableStateListOf(UserAPICollection(
                    userAPIs = arrayListOf(UserAPI(
                        endPoint = "https://world.openfoodfacts.net/api/v2/product/788434115681",
                    ))
                ))}
                val responseResult = remember {mutableStateOf("")}
                val selectedCollectionIndex = remember{mutableIntStateOf(0)}
                val collectionName = remember {mutableStateOf(userAPICollections[selectedCollectionIndex.intValue].internalCollection!!.collectionName)}
                val collectionNames = remember{userAPICollections.map {
                    it.internalCollection!!.collectionName
                }.toMutableStateList()}
                val selectedEndpointIndex = remember{mutableIntStateOf(0)}
                val endPoint = remember {mutableStateOf(userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint)}
                val endpointNames = remember{userAPICollections[selectedCollectionIndex.intValue].userAPIs.map {
                    it.endPoint
                }.toMutableStateList()}
                Row {
                    TextfieldDropdownMenu(collectionName, selectedCollectionIndex,collectionNames, "Collection", 16.dp, Modifier.weight(1f), onClick = {
                        responseResult.value = ""
                        if (it >= userAPICollections.size) {
                            //TODO replace with dao call
                            userAPICollections.add(UserAPICollection())
                            selectedCollectionIndex.intValue = it
                            collectionNames.add(
                                index = selectedCollectionIndex.intValue,
                                element = userAPICollections[selectedCollectionIndex.intValue].internalCollection!!.collectionName
                            )
                        }
                        else {
                            selectedCollectionIndex.intValue = it
                        }
                        selectedEndpointIndex.intValue = 0
                        collectionName.value = userAPICollections[selectedCollectionIndex.intValue].internalCollection!!.collectionName
                        endPoint.value = userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint
                    })
                }

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
                    TextfieldDropdownMenu(endPoint, selectedEndpointIndex, endpointNames, "Endpoint", 16.dp, Modifier.weight(1f), onClick = {
                        responseResult.value = ""
                        if (it >= userAPICollections[selectedCollectionIndex.intValue].userAPIs.size) {
                            //TODO replace with dao call
                            userAPICollections[selectedCollectionIndex.intValue].userAPIs.add(UserAPI())
                            Log.d("Home", userAPICollections[selectedCollectionIndex.intValue].userAPIs.toString())
                            selectedEndpointIndex.intValue = it
                            endpointNames.add(
                                index = selectedEndpointIndex.intValue,
                                element = userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint
                            )
                        }
                        else {
                            selectedEndpointIndex.intValue = it
                        }
                        endPoint.value = userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint
                    })
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
                        val newParamKey = remember { mutableStateOf("") }
                        val newParamValue = remember { mutableStateOf("") }
                        val bodyContent = remember {mutableStateOf("")}

                        if (contentTitles[selectedContentIndex.intValue] == RequestContent.HEADERS.value) {
                            userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].headers.forEach {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(it.key, color = Color.LightGray)
                                    Text(it.value)
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
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].headers.put(newHeaderKey.value, newHeaderValue.value)
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
                            userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].params.entries.forEach {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(it.key, color = Color.LightGray)
                                    Text(it.value)
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
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].params.put(newParamKey.value, newParamValue.value)
                                    newParamKey.value = ""
                                    newParamValue.value = ""
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "More options")
                                }
                            }
                        }
                    }
                }
                IconButton(onClick = {
                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].execute {
                        Log.d("Home", it.toString())
                        responseResult.value = it.toString() + "\n" + it.body.string()
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Run")
                }
                if (responseResult.value.isNotEmpty()) {
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
                            Text(responseResult.value)
                        }
                    }
                }
            }
        }
    }
}