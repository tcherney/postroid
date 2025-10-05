package com.tcherney.postroid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.Dp

@Composable
fun TextfieldDropdownMenu(value: MutableState<String>, selectedIndex: MutableIntState, menuOptions: SnapshotStateList<String>, placeHolderText: String, padding: Dp, modifier: Modifier = Modifier, newLabel: String = "New", onClick: (Int) -> Unit = {}, onFocusLost: () -> Unit = {}) {
    val expanded = remember{ mutableStateOf(false) }
    Column(modifier = Modifier.padding(start = padding, top = padding)) {
        Row {
            TextField(
                value = value.value,
                onValueChange = { value.value = it },
                modifier = modifier.fillMaxWidth().onFocusChanged {
                    if (it.hasFocus) {
                        onFocusLost()
                    }
                },
                placeholder = { Text(placeHolderText) }
            )
            IconButton(onClick = { expanded.value = !expanded.value }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
            }
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            menuOptions.forEachIndexed { i, label ->
                if (i != selectedIndex.intValue) {
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            onClick(i)
                            expanded.value = false
                        }
                    )
                }
            }
            DropdownMenuItem(
                text = { Text(newLabel) },
                onClick = {
                    onClick(menuOptions.size)
                    expanded.value = false

                }
            )
        }
    }
}