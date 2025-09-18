package com.tcherney.postroid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabeledDropdownMenu(labels: List<String>) {
    val selectedIndex = remember{ mutableIntStateOf(0) }
    val expanded = remember{mutableStateOf(false)}
    val selectedLabel = remember{mutableStateOf(labels[selectedIndex.intValue])}
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Button(onClick = { expanded.value = !expanded.value }) {
            Row {
                Text(selectedLabel.value)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
            }
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            labels.forEachIndexed { i, label ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        selectedLabel.value = label
                        selectedIndex.intValue = i
                        expanded.value = false
                    }
                )
            }
        }
    }
}