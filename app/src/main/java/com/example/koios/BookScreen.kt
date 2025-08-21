package com.example.koios

import android.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.koios.ui.theme.Beige
import com.example.koios.ui.theme.DarkRed
import com.example.koios.ui.theme.Darkbeige
import com.example.koios.ui.theme.Darkblue
import com.example.koios.ui.theme.Darkgrey
import com.example.koios.ui.theme.Green
import com.example.koios.ui.theme.LightBlue
import com.example.koios.ui.theme.LightWithe
import com.example.koios.ui.theme.MiddeBlue
import kotlinx.coroutines.launch

@Composable
fun BookScreen(
    state: BookState,
    onEvent: (BookEvent) -> Unit,

){
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        floatingActionButton = {
            Column {
                if(listState.canScrollBackward)
                    FloatingActionButton(onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)}
                    }
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Add book", Modifier.size(30.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                FloatingActionButton(onClick = {
                    onEvent(BookEvent.ShowDialog)
                }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add book", Modifier.size(30.dp))
                }
            }
        },
        modifier = Modifier.padding(0.dp)

    ){ padding->
        if(state.isAddingBook or state.isChangeBook){
            AddBookDialog(state = state, onEvent = onEvent)
        }
        Column (
            modifier = Modifier.padding(PaddingValues(0.dp,0.dp,0.dp,0.dp))
                .background(color = Darkblue)
                .fillMaxSize()

        ){
            Spacer(Modifier.height(50.dp))
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .height(120.dp)
                    .fillMaxSize()
                    .background(
                        color = LightBlue,
                        shape = RoundedCornerShape(20))
                    .border(5.dp, color = MiddeBlue, shape = RoundedCornerShape(20))
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
                            Text(text = "Buch suchen", color = androidx.compose.ui.graphics.Color.Gray,fontWeight = FontWeight.Bold)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                tint = androidx.compose.ui.graphics.Color.Gray,
                                contentDescription = "Search icon"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onEvent(BookEvent.OnSearchTextChange(""))
                                }
                            )
                            {
                                if(!state.searchText.isBlank())
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        tint = androidx.compose.ui.graphics.Color.Gray,
                                        contentDescription = "Search icon"
                                    )
                            }
                        },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier
                            .height(55.dp)
                            .fillMaxWidth()
                            .padding(0.dp),
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = LightWithe, focusedContainerColor = LightWithe,
                            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent, focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent),

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp,0.dp,20.dp,0.dp)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(!state.books.isEmpty())
                    {
                        if(!state.books.none { book -> book.condition == 0 })
                            {
                                Row(
                                    modifier = Modifier
                                        .background(color = Darkbeige, RoundedCornerShape(7.dp)),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                )
                                {
                                    Text(
                                        text = state.books.filter { book -> book.condition == 0 }.size.toString(),
                                        color = Darkgrey,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(5.dp,2.dp)
                                    )
                                }
                            }

                        if(!state.books.none { book -> book.condition == 1 })
                            {
                                Spacer(Modifier.width(5.dp))
                                Row(
                                    modifier = Modifier
                                        .background(color =DarkRed, RoundedCornerShape(7.dp)),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                )
                                {
                                    Text(
                                        text = state.books.filter { book -> book.condition == 1 }.size.toString(),
                                        color = Darkblue,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(5.dp,2.dp)
                                    )
                                }
                            }

                        if(!state.books.none { book -> book.condition == 2 })
                            {
                                Spacer(Modifier.width(5.dp))
                                Row(
                                    modifier = Modifier
                                        .background(color = Green, RoundedCornerShape(7.dp)),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                )
                                {
                                    Text(
                                        text = state.books.filter { book -> book.condition == 2 }.size.toString(),
                                        color = Darkgrey,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(5.dp,2.dp)
                                    )
                                }
                            }

                        Spacer(Modifier.width(5.dp))
                        Row(
                            modifier = Modifier
                                .background(color = Darkblue, RoundedCornerShape(7.dp)),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(
                                text = state.books.size.toString(),
                                color = Darkbeige,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(5.dp,2.dp)
                            )
                        }
                    }

                    LazyColumn (

                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        item {
                            Row (
                                modifier = Modifier
                                    .height(40.dp)
                                    .horizontalScroll(rememberScrollState()),
                                verticalAlignment = Alignment.CenterVertically,
                            ){
                                SortType.values().forEach { sortType ->
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        RadioButton(
                                            selected = state.sortType == sortType,
                                            onClick = {
                                                onEvent(BookEvent.SortBooks(sortType))
                                            }
                                        )

                                        var iconcolor = androidx.compose.ui.graphics.Color(Color.DKGRAY)
                                        if(state.sortType == sortType)
                                            iconcolor = androidx.compose.ui.graphics.Color(Color.BLACK)

                                        if(sortType == SortType.TITLE)
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Title Filter" , tint = iconcolor)
                                        if(sortType == SortType.AUTHOR)
                                            Icon(imageVector = Icons.Default.Person, contentDescription = "Author Filter" , tint =iconcolor)
                                        if(sortType == SortType.RATING)
                                            Icon(imageVector = Icons.Default.Star, contentDescription = "Rating Filter" , tint = iconcolor)
                                        if(sortType == SortType.CONDITION)
                                            Icon(imageVector = Icons.Default.Info, contentDescription = "Condition Filter" , tint = iconcolor)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(state.books.isEmpty())
                Image(
                    painter = painterResource(R.drawable.outline_search_off_24),
                    contentDescription = "Condition Filter" ,
                    modifier = Modifier
                        .size(300.dp)
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .padding(0.dp,50.dp,0.dp,0.dp)
                    )


            LazyColumn(
                state = listState
            ) {
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
    var expanded by remember { mutableStateOf(false) }
    var cardcolor = Darkbeige
    if(book.condition == 1){
        cardcolor = DarkRed
    }
    if(book.condition == 2) {
        cardcolor = Green
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(color = Darkblue),
        shape = RoundedCornerShape(0.dp),
    ) {
        Column(
            modifier = Modifier
                .background(color = Darkblue)
                .padding(0.dp),
        ) {
            if(expanded)
                Box(
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 5.dp)
                        .align(Alignment.End),
                )
                {
                    Row {
                        if(!book.urllink.isBlank())
                        {
                            IconButton(onClick = {
                                onEvent(BookEvent.LoadURL(book.urllink))

                            },
                                modifier = Modifier.background(color = Darkgrey, shape = RoundedCornerShape(20.dp)),
                            ) {
                                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Go to  url" , tint = Darkbeige)
                            }
                            Spacer(Modifier.width(10.dp))
                        }
                        IconButton(onClick = {
                            onEvent(BookEvent.ChangeBook(book))

                        },
                            modifier = Modifier.background(color = Darkgrey, shape = RoundedCornerShape(20.dp)),
                        ) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "Change book" , tint = Darkbeige)
                        }
                        Spacer(Modifier.width(10.dp))
                        IconButton(onClick = {
                            onEvent(BookEvent.DeleteBook(book))

                        },
                            modifier = Modifier.background(color = Darkgrey, shape = RoundedCornerShape(20.dp)),
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete book" , tint = Darkbeige)
                        }
                    }
                }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp,0.dp,10.dp,5.dp)
                    .background(color = Darkblue),
                horizontalArrangement = Arrangement.Start

            ){
                Text(
                    text = book.title,
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .align(alignment = Alignment.CenterVertically)
                        .width(320.dp),
                    color = Darkbeige,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                )
                {
                    IconButton(
                        onClick = { expanded = !expanded},
                        modifier = Modifier
                            .background(color = Darkgrey, shape = RoundedCornerShape(20.dp))
                    ) {
                        if(!expanded)
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Delete book" , tint = Darkbeige)
                        else
                            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Delete book" , tint = Darkbeige)
                    }
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = androidx.compose.ui.graphics.Color(Color.TRANSPARENT)),
                    horizontalArrangement = Arrangement.Start,
                ){
                }
            }
            Card(
                shape = RoundedCornerShape(10,10,30,30),
                border = BorderStroke(width = 4.dp,color = Beige),
                modifier = Modifier.padding(5.dp).background(color = Darkblue)
            )
            {
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = cardcolor),
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
                                    tint = Darkgrey,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = book.author,
                                    modifier = Modifier
                                        .padding(5.dp,0.dp),
                                    color = Darkgrey,
                                    fontWeight = FontWeight.Black
                                )
                            }

                        }
                        Row {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(170.dp))  {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Author of the book",
                                    tint = Darkgrey,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = convert_state(book.condition),
                                    modifier = Modifier
                                        .padding(5.dp,0.dp),
                                    color = Darkgrey,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            if(convert_rate(book.rating) != "") {
                                Row (verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Author of the book",
                                        tint = Darkgrey,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(
                                        text = convert_rate(book.rating),
                                        modifier = Modifier
                                            .padding(5.dp,0.dp),
                                        color = Darkblue,
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