package com.gunishjain.skribbleapp.ui.rooms

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview
@Composable
fun CreateRoom() {

    var roomFieldState by remember {
        mutableStateOf("")
    }

    var userFieldState by remember {
        mutableStateOf("")
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Create Room",
        fontSize = 22.sp)
        
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = roomFieldState,
            label = { Text(text = "Enter Room Name") },
            onValueChange = { roomFieldState=it },
            singleLine = true,
             modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = userFieldState,
            label = { Text(text = "Enter Your Name") },
            onValueChange = { userFieldState=it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(labelText = "Select Max Rounds", contentArray = arrayListOf("2","3","4","5"))
        Spacer(modifier = Modifier.height(5.dp))
        ExposedDropdownMenuBox(labelText = "Select Room Size", contentArray = arrayListOf("2","3","4","5"))
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Create Room")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExposedDropdownMenuBox(
    labelText :String,
    contentArray : ArrayList<String>
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(labelText) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
//                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                contentArray.forEach { item ->
                    DropdownMenuItem(
                        content = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}