package com.example.koios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.ticker

@Composable
fun AddBookDialog(
    state: BookState,
    onEvent: (BookEvent) -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(BookEvent.HideDialog)
        },
        title = {
            Text(text = "Add book")
                },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                TextField(
                    value = state.title,
                    onValueChange = {
                        onEvent(BookEvent.SetTitle(it))
                    },
                    placeholder = {
                        Text(text = "Titel")
                    }
                )

            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ){
                Button(onClick = {
                    onEvent(BookEvent.SaveBook)
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}