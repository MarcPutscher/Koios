package com.example.koios

import android.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookScreen(
    state: BookState,
    onEvent: (BookEvent) -> Unit
){
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(BookEvent.ShowDialog)
            }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add book")
            }
        },
        modifier = Modifier.padding(10.dp)

    ){ padding->
        if(state.isAddingBook){
            AddBookDialog(state = state, onEvent = onEvent)
        }
        Column (
            modifier = Modifier.padding(PaddingValues(0.dp,50.dp,0.dp,0.dp))
                .background(color = androidx.compose.ui.graphics.Color(Color.rgb(51, 52, 70)))

        ){
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .height(100.dp)
                    .fillMaxSize()
                    .background(
                        color = androidx.compose.ui.graphics.Color(Color.rgb(127, 140, 170)),
                        shape = RoundedCornerShape(20))
                    .border(5.dp, color = androidx.compose.ui.graphics.Color(Color.rgb(54, 73, 117)), shape = RoundedCornerShape(20))

            )
            {
                LazyColumn (
                    contentPadding = padding,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    item {
                        Row (
                            modifier = Modifier
                                .fillMaxSize()
                                .horizontalScroll(rememberScrollState()),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            SortType.values().forEach { sortType ->
                                Row (
                                    modifier = Modifier
                                        .clickable{
                                            onEvent(BookEvent.SortBooks(sortType))
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    RadioButton(
                                        selected = state.sortType == sortType,
                                        onClick = {
                                            onEvent(BookEvent.SortBooks(sortType))
                                        }
                                        )
                                    Text(text = sortType.name)
                                }
                            }
                        }
                    }
                }

            }
            LazyColumn {

                items(state.books){ book ->
                    Item_template(book,onEvent)
                }
            }

        }
    }
}

@Composable
fun convert_state(condition: Int = 0): String {
    if(condition == 1){
        return "Im Besitz"
    }
    if(condition == 2){
        return "Schon gelesen"
    }
    return  "Noch kaufen"
}

@Composable
fun convert_rate(rateing: Int = -1): String {
    if(rateing != -1){
        return rateing.toString()
    }
    return  ""
}

@Composable
fun Item_template(book: Book, onEvent: (BookEvent) -> Unit) {
    var color = Color.rgb(139,137,137)
    if(book.condition == 1){
        color = Color.rgb(218,165,32)
    }
    if(book.condition == 2) {
        color = Color.rgb(0,139,0)
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        shape = RoundedCornerShape(30),
        border = BorderStroke(width = 4.dp,color = androidx.compose.ui.graphics.Color(Color.rgb(54, 73, 117)))


    ) {
        Row (
            modifier = Modifier
                .padding(1.dp)
                .fillMaxSize()
                .background(color = androidx.compose.ui.graphics.Color(color)),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.baseline_menu_book_24),
                contentDescription = "Photo of Book",
                modifier = Modifier
                    .width(100.dp)
                    .height(90.dp)
                //.background(color = androidx.compose.ui.graphics.Color(Color.RED))
            )

            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .align(Alignment.Top)
                //.background(color = androidx.compose.ui.graphics.Color(Color.BLUE))
            ) {
                Column {
                    Text(
                        text = "Titel: ",
                        modifier = Modifier
                            .padding(5.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Autor: ",
                        modifier = Modifier
                            .padding(5.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Status: ",
                        modifier = Modifier
                            .padding(5.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    if(book.rating != -1) {
                        Text(
                            text = "Ranking: ",
                            modifier = Modifier
                                .padding(5.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
                Column {
                    Text(
                        text = book.title,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Text(
                        text = book.author,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Text(
                        text = convert_state(book.condition),
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    if(book.rating != -1) {
                        Text(
                            text = convert_rate(book.rating),
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                }
                IconButton(onClick = {
                    onEvent(BookEvent.DeleteBook(book))
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete book")
                }
            }
        }
    }

}