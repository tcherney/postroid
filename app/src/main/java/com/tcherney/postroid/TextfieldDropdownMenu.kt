package com.tcherney.postroid

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun TextfieldDropdownMenu(value: MutableState<String>, selectedIndex: MutableIntState, menuOptions: SnapshotStateList<String>, placeHolderText: String, padding: Dp, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val expanded = remember{ mutableStateOf(false) }
    TextField(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier.fillMaxWidth().padding(start = padding, top = padding, bottom = padding),
        placeholder = { Text(placeHolderText) }
    )
    IconButton(onClick = { expanded.value = !expanded.value }) {
        Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
    }
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        menuOptions.forEachIndexed { i, label ->
            if (i != 0) {
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        value.value = menuOptions[i]
                        selectedIndex.intValue = i
                        expanded.value = false
                        onClick()
                    }
                )
            }
        }
    }
}