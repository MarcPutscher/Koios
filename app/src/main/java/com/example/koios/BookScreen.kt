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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var backgroundcolor =Color.rgb(34, 40, 49)
var backgroundcolor2 =Color.rgb(57, 62, 70)
var backgroundcolor3 =Color.rgb(148, 137, 121)
var backgroundcolor4 =Color.rgb(223, 208, 184)
@Composable
fun BookScreen(
    state: BookState,
    onEvent: (BookEvent) -> Unit,

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
        modifier = Modifier.padding(0.dp)

    ){ padding->
        if(state.isAddingBook or state.isChangeBook){
            AddBookDialog(state = state, onEvent = onEvent)
        }
        Column (
            modifier = Modifier.padding(PaddingValues(0.dp,50.dp,0.dp,0.dp))
                .background(color = androidx.compose.ui.graphics.Color(backgroundcolor))
                .fillMaxSize()

        ){
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .height(120.dp)
                    .fillMaxSize()
                    .background(
                        color = androidx.compose.ui.graphics.Color(Color.rgb(127, 140, 170)),
                        shape = RoundedCornerShape(20))
                    .border(5.dp, color = androidx.compose.ui.graphics.Color(Color.rgb(54, 73, 117)), shape = RoundedCornerShape(20))
            )
            {
                Box(
                    modifier = Modifier
                        .background(color = androidx.compose.ui.graphics.Color.Transparent, shape = RoundedCornerShape(0.dp))
                        .padding(15.dp,15.dp,15.dp,0.dp)
                )
                {
                    TextField(
                        shape = RoundedCornerShape(10.dp),
                        value = state.searchText,
                        onValueChange = {
                            onEvent(BookEvent.OnSearchTextChange(it))
                        },
                        placeholder = {
                            Text(text = "Buch suchen")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                tint = androidx.compose.ui.graphics.Color.Gray,
                                contentDescription = "Search icon"
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                tint = androidx.compose.ui.graphics.Color.Gray,
                                contentDescription = "Search icon"
                            )
                        },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier
                            .height(55.dp)
                            .fillMaxWidth()
                            .padding(0.dp)

                    )
                }
                Row (
                    modifier = Modifier
                        .padding(15.dp,5.dp,15.dp,0.dp)

                ){

                    Image(
                        painter = painterResource(id = R.drawable.outline_filter_alt_24),
                        contentDescription = "Filter book",
                        modifier = Modifier
                            .height(30.dp))

                    LazyColumn (

                        modifier = Modifier.fillMaxSize()
                            .height(50.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        item {
                            Row (
                                modifier = Modifier
                                    .height(30.dp)
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
fun convert_rate(rating: Int = -1): String {
    if(0 <= rating && rating <= 10){
        return rating.toString()
    }
    return  ""
}

@Composable
fun Item_template(book: Book, onEvent: (BookEvent) -> Unit) {

    var color = backgroundcolor3
    if(book.condition == 1){
        color = Color.rgb(165, 91, 75)
    }
    if(book.condition == 2) {
        color = Color.rgb(0,139,0)
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(color = androidx.compose.ui.graphics.Color(backgroundcolor)),
    ) {
        Column(
            modifier = Modifier
                .background(color = androidx.compose.ui.graphics.Color(backgroundcolor)),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp,0.dp,10.dp,5.dp),
                horizontalArrangement = Arrangement.End

            ){
                Text(
                    text = book.title,
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .align(alignment = Alignment.CenterVertically)
                        .width(260.dp),
                    color = androidx.compose.ui.graphics.Color(backgroundcolor3),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Row (
                    modifier = Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.End
                ){
                    IconButton(onClick = {
                        onEvent(BookEvent.ChangeBook(book))

                    },
                        modifier = Modifier.background(color = androidx.compose.ui.graphics.Color(backgroundcolor2), shape = RoundedCornerShape(20.dp)),
                    ) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Change book" , tint = androidx.compose.ui.graphics.Color(backgroundcolor3))
                    }
                    Spacer(Modifier.width(10.dp))
                    IconButton(onClick = {
                        onEvent(BookEvent.DeleteBook(book))

                    },
                        modifier = Modifier.background(color = androidx.compose.ui.graphics.Color(backgroundcolor2), shape = RoundedCornerShape(20.dp)),
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete book" , tint = androidx.compose.ui.graphics.Color(backgroundcolor3))
                    }
                }
            }
            Card(
                shape = RoundedCornerShape(10,10,30,30),
                border = BorderStroke(width = 4.dp,color = androidx.compose.ui.graphics.Color(backgroundcolor4)),
                modifier = Modifier.padding(5.dp).background(color = androidx.compose.ui.graphics.Color(backgroundcolor))
            )
            {
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = androidx.compose.ui.graphics.Color(color)),
                    verticalAlignment = Alignment.CenterVertically,

                    ){
                    Image(
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = "Photo of Book",
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .padding(10.dp)
                    )

                    Column(horizontalAlignment = AbsoluteAlignment.Left, modifier = Modifier.padding(5.dp)) {

                        if(!book.author.isBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Author of the book",
                                    tint = androidx.compose.ui.graphics.Color(backgroundcolor2),
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = book.author,
                                    modifier = Modifier
                                        .padding(5.dp,0.dp),
                                    color = androidx.compose.ui.graphics.Color(backgroundcolor),
                                    fontWeight = FontWeight.Black
                                )
                            }

                        }
                        Row {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(170.dp))  {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Author of the book",
                                    tint = androidx.compose.ui.graphics.Color(backgroundcolor2),
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = convert_state(book.condition),
                                    modifier = Modifier
                                        .padding(5.dp,0.dp),
                                    color = androidx.compose.ui.graphics.Color(backgroundcolor),
                                    fontWeight = FontWeight.Black
                                )
                            }
                            if(convert_rate(book.rating) != "") {
                                Row (verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Author of the book",
                                        tint = androidx.compose.ui.graphics.Color(backgroundcolor2),
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(
                                        text = convert_rate(book.rating),
                                        modifier = Modifier
                                            .padding(5.dp,0.dp),
                                        color = androidx.compose.ui.graphics.Color(backgroundcolor),
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}