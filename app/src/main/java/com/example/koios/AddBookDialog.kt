package com.example.koios


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.koios.ui.theme.Darkbeige
import com.example.koios.ui.theme.Darkgrey
import com.example.koios.ui.theme.LightBlue
import com.example.koios.ui.theme.LightWithe

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
        containerColor = Darkgrey,
        title = {
            var title = "Buch hinzufügen"
            if(state.id != 0)
                title = "Buch ändern"
            Text(
                text = title,
                fontWeight = FontWeight.Black,
                fontSize = 30.sp,
                color = Darkbeige
            ) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TextField(
                    value = state.title,
                    onValueChange = {
                        onEvent(BookEvent.SetTitle(it))
                    },
                    placeholder = {
                        Text(text = "Titel", color = Color.Gray,fontWeight = FontWeight.Bold)
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = false,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = LightWithe, focusedContainerColor = LightWithe,
                        unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent),
                )
                TextField(
                    value = state.author,
                    onValueChange = {
                        onEvent(BookEvent.SetAuthor(it))
                    },
                    placeholder = {
                        Text(text = "Autor", color = Color.Gray,fontWeight = FontWeight.Bold)
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = LightWithe, focusedContainerColor = LightWithe,
                        unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent),
                )
                TextField(
                    value = state.urllink,
                    onValueChange = {
                        onEvent(BookEvent.SetURLLink(it))
                    },
                    placeholder = {
                        Text(text = "URL", color = Color.Gray,fontWeight = FontWeight.Bold)
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = LightWithe, focusedContainerColor = LightWithe,
                        unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent),
                )
                Spacer(Modifier.height(5.dp))
                Column (
                    modifier = Modifier
                        .background(
                            color = LightBlue,
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
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(
                        modifier = Modifier
                            .padding(0.dp,5.dp,0.dp,0.dp)
                            .background(
                                color = LightBlue,
                                shape = RoundedCornerShape(20)),
                        horizontalAlignment = Alignment.Start

                    ){
                        Column(
                            modifier = Modifier
                                .height(170.dp)
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
                            listOf(0,1,2).forEach { conditionType ->
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
                    Image(
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = "Photo of Book",
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .padding(0.dp)
                            .clickable( onClick = {
                                onEvent(BookEvent.SetImage)
                            }),
                        alignment = Alignment.Center
                    )
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