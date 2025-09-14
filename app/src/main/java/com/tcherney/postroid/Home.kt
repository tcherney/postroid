package com.tcherney.postroid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.tcherney.postroid.ui.theme.PostroidTheme


@Composable
fun Home(
    modifier: Modifier = Modifier, navController: NavHostController? = null
) {
    PostroidTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f, false)
                )
                {
                    Text(
                        text = "hello world",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}