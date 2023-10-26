package com.gunishjain.skribbleapp.ui.rooms

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.toJson


@Composable
fun CreateRoom(
    navController: NavController
) {

    var roomFieldState by remember {
        mutableStateOf("")
    }

    var userFieldState by remember {
        mutableStateOf("")
    }

    var maxRounds by remember { mutableStateOf(2) } // Default value
    var roomSize by remember { mutableStateOf(2) }

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

        ExposedDropdownMenuBox(
            labelText = "Select Max Rounds",
            contentArray = arrayListOf(2,3,4,5),
            selectedData = maxRounds,
            onItemSelected = {maxRounds = it}
        )

        Spacer(modifier = Modifier.height(5.dp))

        ExposedDropdownMenuBox(
            labelText = "Select Room Size",
            contentArray = arrayListOf(2,3,4,5),
            selectedData = roomSize,
            onItemSelected = {roomSize = it}
        )

        Spacer(modifier = Modifier.height(5.dp))

        Button(onClick = {

            if(roomFieldState.isNotEmpty() and userFieldState.isNotEmpty()){
                val room = Room(roomFieldState, userFieldState, maxRounds, roomSize)
                val model = room.toJson()
                navController.navigate(route = "paint_screen/item/$model")
            }
            else {
                Log.d("Gunish","Fill all values")
            }

        }) {
            Text(text = "Create Room")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExposedDropdownMenuBox(
    labelText :String,
    contentArray : ArrayList<Int>,
    selectedData: Int,
    onItemSelected :(Int)->Unit

) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf(labelText) }

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
                value = selectedData.toString(),
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
                        content = { Text(text = item.toString()) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}