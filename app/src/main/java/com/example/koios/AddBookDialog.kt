package com.example.koios

import android.R
import android.graphics.Color
import android.widget.NumberPicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddBookDialog(
    state: BookState,
    onEvent: (BookEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier.height(600.dp),
        onDismissRequest = {
            onEvent(BookEvent.HideDialog)
        },
        title = { Text(text = "Buch hinzufügen") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                TextField(
                    value = state.author,
                    onValueChange = {
                        onEvent(BookEvent.SetAuthor(it))
                    },
                    placeholder = {
                        Text(text = "Autor")
                    }
                )
                TextField(
                    value = state.urllink,
                    onValueChange = {
                        onEvent(BookEvent.SetURLLink(it))
                    },
                    placeholder = {
                        Text(text = "URL")
                    }
                )
                Spacer(Modifier.height(5.dp))
                Column (
                    modifier = Modifier
                        .background(
                            color = androidx.compose.ui.graphics.Color(Color.rgb(127, 140, 170)),
                            shape = RoundedCornerShape(20))
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bewertung: "+ state.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Slider(
                        modifier = Modifier.padding(10.dp,10.dp,10.dp,0.dp),
                        value = state.rating.toFloat(),
                        onValueChange = {
                            onEvent(BookEvent.SetRating(it.toInt())) },
                        valueRange = -1f..10f,
                        steps = 11
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(0.dp,5.dp,0.dp,0.dp)
                        .background(
                            color = androidx.compose.ui.graphics.Color(Color.rgb(127, 140, 170)),
                            shape = RoundedCornerShape(20)),
                    horizontalAlignment = Alignment.Start

                ){
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(170.dp)
                            .padding(10.dp,0.dp),
                        horizontalAlignment = Alignment.Start
                    ){
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(0.dp,5.dp,0.dp,2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = "Status",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                            )
                        }

                        listOf<Int>(0,1,2).forEach { conditionType ->
                            Row (
                                modifier = Modifier
                                    .clickable{
                                        onEvent(BookEvent.SetCondition(conditionType))
                                    }
                                    .height(28.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                RadioButton(
                                    selected = state.condition == conditionType,
                                    onClick = {
                                        onEvent(BookEvent.SetCondition(conditionType))
                                    }
                                )
                                var text = "Noch kaufen"
                                if(conditionType == 1){
                                    text = "Im Besitz"
                                }
                                if(conditionType == 2){
                                    text = "Schon gelesen"
                                }
                                Text(text = text)
                            }
                        }


                    }
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(BookEvent.SaveBook)
                }) {
                    Text(text = "Speichern")
                }
            }
        },
    )
}