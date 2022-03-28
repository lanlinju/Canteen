package com.example.canteen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.canteen.R
import com.example.canteen.theme.ui.CanteenM3Theme

@Composable
fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "Functionality not available \uD83D\uDE48",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CLOSE")
            }
        }
    )
}

@Composable
fun showAlert(text: String, onConfirmClick: () -> Unit = {},onDismiss: () -> Unit) {

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "提示")
            },
            text = {
                Text(text = text)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmClick()
                        onDismiss()
                    }
                ) {
                    Text(stringResource(id = R.string.sure))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )


}

@Preview(showBackground = true, name = "ShowDialog", widthDp = 320)
@Composable
fun ShowAlertDialogPreview() {
    CanteenM3Theme {
        val openDialog = remember { mutableStateOf(true) }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            // val centerWithPaddingModifier = Modifier.align(Alignment.CenterHorizontally)

            if(openDialog.value){
                showAlert(text = "hahahah", onDismiss = {openDialog.value = false})
            }
            Button(onClick = { openDialog.value = !openDialog.value }) {
                Text(text = "ShowAlert")
            }
            SmallFloatingActionButton(
                onClick = { /* do something */ },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        }
    }
}

@Composable
fun BottomBar() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Songs", "Artists", "Playlists")

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen() {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Simple TopAppBar") },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val list = (0..75).map { it.toString() }
                items(count = list.size) {
                    Text(
                        text = list[it],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        },
        bottomBar = { BottomBar() }
    )
}
