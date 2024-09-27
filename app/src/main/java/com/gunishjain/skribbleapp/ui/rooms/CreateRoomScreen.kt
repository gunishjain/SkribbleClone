package com.gunishjain.skribbleapp.ui.rooms

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.toJson
import com.gunishjain.skribbleapp.navigation.Screen
import com.gunishjain.skribbleapp.ui.viewmodels.CreateRoomViewModel
import com.gunishjain.skribbleapp.ui.viewmodels.LobbyViewModel


@Composable
fun CreateRoomScreen(
    navController: NavController
) {

    val viewModel: CreateRoomViewModel = hiltViewModel()
    val roomCreated by viewModel.roomCreated.collectAsState(initial = null)


    var roomFieldState by remember {
        mutableStateOf("")
    }

    var userFieldState by remember {
        mutableStateOf("")
    }

    var maxRounds by remember { mutableStateOf(2) } // Default value
    var roomSize by remember { mutableStateOf(2) }

    LaunchedEffect(roomCreated) {
        roomCreated?.let {room->
            navController.navigate(Screen.Lobby.passRoomName(room.name)) // Navigate to lobby when room is created
        }
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

            viewModel.createRoom(roomFieldState, userFieldState,  roomSize , maxRounds)

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