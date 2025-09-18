package com.tcherney.postroid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.tcherney.postroid.ui.theme.PostroidTheme


@Composable
fun Home(
    modifier: Modifier = Modifier, navController: NavHostController? = null
) {
    PostroidTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                //TODO figure out how we want these to look
                LabeledDropdownMenu(listOf("Get", "Post", "Delete", "Put"))
                LabeledBox(label = {
                    Row(modifier = Modifier.background(Color.LightGray).fillMaxWidth().clickable(onClick = {
                        //TODO need headers/body/params
                    })) {
                        Text(
                            text = "Headers"
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
                    }
                }) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(1f, false)
                    ) {
                        val newHeaderKey = remember { mutableStateOf("") }
                        val newHeaderValue = remember { mutableStateOf("") }
                        val headers = remember { mutableStateListOf<Pair<String,String>>()}
                        headers.forEach {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                                Text(it.first, color = Color.LightGray)
                                Text(it.second)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = newHeaderKey.value,
                                onValueChange = { newHeaderKey.value = it.trim() },
                                modifier = Modifier.weight(1f),
                                placeholder = {Text("Key")}
                            )
                            TextField(
                                value = newHeaderValue.value,
                                onValueChange = { newHeaderValue.value = it.trim() },
                                modifier = Modifier.weight(1f),
                                placeholder = {Text("Value")}
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
                }
            }
        }
    }
}