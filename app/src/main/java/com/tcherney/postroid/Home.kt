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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                //example "https://world.openfoodfacts.net/api/v2/product/788434115681"
                val userAPICollections by userAPIViewModel.userAPIs.collectAsState(initial = listOf(
                    UserAPICollection(
                        internalCollection = UserAPICollectionInternal(
                            collectionName = "Loading"
                        ),
                        userAPIs = listOf(UserAPI(
                            endPoint = "Loading"
                        ))
                    )
                ))
                val collectionAdded = remember { mutableStateOf(false) }
                val endpointAdded = remember { mutableStateOf(false) }
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
                LaunchedEffect(userAPICollections) {
                    Log.d("Home", "Updated $collectionAdded $userAPICollections")
                    if (userAPICollections.isEmpty()) {
                        collectionAdded.value = true
                        userAPIViewModel.addAPICollection(UserAPICollection(
                            internalCollection = UserAPICollectionInternal()
                        ))
                    }
                    else {
                        if (collectionAdded.value) {
                            Log.d("Home", "Wait what")
                            selectedCollectionIndex.intValue = userAPICollections.size - 1
                            collectionAdded.value = false
                        }
                        if (endpointAdded.value) {
                            selectedEndpointIndex.intValue =
                                userAPICollections[selectedCollectionIndex.intValue].userAPIs.size - 1
                            endpointAdded.value = false
                        }
                        Log.d("Home", "Before $collectionNames")
                        collectionNames.clear()
                        collectionNames.addAll(userAPICollections.map {
                            it.internalCollection!!.collectionName
                        })
                        if (userAPICollections.isNotEmpty()) {
                            endpointNames.clear()
                            endpointNames.addAll(userAPICollections[selectedCollectionIndex.intValue].userAPIs.map {
                                it.endPoint
                            })
                        }
                        Log.d("Home", "After $collectionNames")
                        if (userAPICollections.isNotEmpty()) {
                            if (collectionName.value != userAPICollections[selectedCollectionIndex.intValue].internalCollection!!.collectionName) {
                                collectionName.value =
                                    userAPICollections[selectedCollectionIndex.intValue].internalCollection!!.collectionName
                            }
                        }
                        if (userAPICollections.isNotEmpty()) {
                            if (userAPICollections[selectedCollectionIndex.intValue].userAPIs.isNotEmpty()) {
                                if (endPoint.value != userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint) {
                                    endPoint.value =
                                        userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint
                                }
                            }
                        }
                    }
                }
                //TODO this adds an entry, fix so we dont need to run this
//                if(userAPICollections.isEmpty()) {
//                    userAPIViewModel.addAPICollection(UserAPICollection(
//                        internalCollection = UserAPICollectionInternal(
//                            collectionName = "Untitled",
//                            collectionID = 1
//                        ),
//                        userAPIs = listOf(UserAPI(endPoint = "", collectionID = 1))
//                    )
//                    )
//                }
                Row {
                    TextfieldDropdownMenu(collectionName, selectedCollectionIndex,collectionNames, "Collection", 16.dp, Modifier.weight(1f), onClick = {
                        responseResult.value = ""
                        if (it >= userAPICollections.size) {
                            collectionAdded.value = true
                            userAPIViewModel.addAPICollection(UserAPICollection(
                                internalCollection = UserAPICollectionInternal()
                            ))
                        }
                        else {
                            Log.d("Home", "Updating indx $selectedCollectionIndex, $it")
                            selectedCollectionIndex.intValue = it
                            collectionName.value = userAPICollections[selectedCollectionIndex.intValue].internalCollection!!.collectionName
                            endPoint.value = userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint
                        }
                        selectedEndpointIndex.intValue = 0
                    },
                        onFocusLost = {
                            if (userAPICollections.isNotEmpty()) {
                                Log.d("Home", "Updating $userAPICollections, $collectionName")
                                userAPICollections[selectedCollectionIndex.intValue].internalCollection!!.collectionName = collectionName.value
                                userAPIViewModel.updateCollection(userAPICollections[selectedCollectionIndex.intValue])
                            }
                            else {
                                Log.d("Home", "Cant update empty")
                            }
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
                            userAPIViewModel.updateCollection(userAPICollections[selectedCollectionIndex.intValue])
                        }
                    )
                    TextfieldDropdownMenu(endPoint, selectedEndpointIndex, endpointNames, "Endpoint", 16.dp, Modifier.weight(1f), onClick = {
                        responseResult.value = ""
                        if (it >= userAPICollections[selectedCollectionIndex.intValue].userAPIs.size) {
                            endpointAdded.value = true
                            userAPIViewModel.addAPI(userAPICollections[selectedCollectionIndex.intValue])
                        }
                        else {
                            selectedEndpointIndex.intValue = it
                            endPoint.value = userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint
                        }
                    },
                        onFocusLost = {
                            if (userAPICollections.isNotEmpty()) {
                                Log.d("Home", "Updating $userAPICollections")
                                if (userAPICollections[selectedCollectionIndex.intValue].userAPIs.isEmpty()) {
                                    userAPIViewModel.updateCollectionWithAPI(
                                        userAPICollections[selectedCollectionIndex.intValue],
                                        UserAPI(
                                            endPoint = endPoint.value,
                                            collectionID = userAPICollections[selectedCollectionIndex.intValue].internalCollection!!.collectionID
                                        )
                                    )
                                } else {
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].endPoint = endPoint.value
                                    userAPIViewModel.updateCollection(userAPICollections[selectedCollectionIndex.intValue])
                            }
                            }
                            else {
                                Log.d("Home", "Cant update empty")
                            }
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
                            if (userAPICollections.isNotEmpty()) {
                                if (userAPICollections[selectedCollectionIndex.intValue].userAPIs.isNotEmpty()) {
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].headers.forEach {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(it.key, color = Color.LightGray)
                                            Text(it.value)
                                        }
                                    }
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
                                    userAPIViewModel.updateCollection(userAPICollections[selectedCollectionIndex.intValue])
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
                            if(userAPICollections.isNotEmpty() and userAPICollections[selectedCollectionIndex.intValue].userAPIs.isNotEmpty()) {
                                if (userAPICollections[selectedCollectionIndex.intValue].userAPIs.isNotEmpty()) {
                                    userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].params.entries.forEach {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(it.key, color = Color.LightGray)
                                            Text(it.value)
                                        }
                                    }
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
                                    userAPIViewModel.updateCollection(userAPICollections[selectedCollectionIndex.intValue])
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "More options")
                                }
                            }
                        }
                    }
                }
                IconButton(onClick = {
                    if(userAPICollections.isNotEmpty()) {
                        userAPICollections[selectedCollectionIndex.intValue].userAPIs[selectedEndpointIndex.intValue].execute {
                            Log.d("Home", it.toString())
                            responseResult.value = it.toString() + "\n" + it.body.string()
                        }
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