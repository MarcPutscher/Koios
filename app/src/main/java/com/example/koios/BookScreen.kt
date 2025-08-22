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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.graphics.vector.ImageVector
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
fun BookScreen(state: BookState, onEvent: (BookEvent) -> Unit,){
    //Variable for the scroll to top function
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    //main layout
    Scaffold (
        floatingActionButton = {
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                //if the main list can scroll to the top then show this button
                if(listState.canScrollBackward)
                    FloatingActionButton(onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)}
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Add book",
                            Modifier.size(30.dp))
                    }

                //the button for adding a new book
                FloatingActionButton(onClick = {
                    onEvent(BookEvent.ShowDialog)
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add book",
                        Modifier.size(30.dp))
                }
            }
        },
        modifier = Modifier.padding(0.dp)

    ){ padding->
        //show the dialog for adding or changing
        if(state.isAddingBook or state.isChangeBook){
            AddBookDialog(state = state, onEvent = onEvent)
        }

        //layout for the taskbar and the main list
        Column (
            modifier = Modifier
                .background(color = Darkblue)
                .fillMaxSize()
        )
        {

            //taskbar
            Taskbar(state,onEvent)

            //if the list is empty then show this image
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

            //main list where the books are displayed
            LazyColumn(
                state = listState
            )
            {
                items(state.books){ book ->
                    ItemTemplate(book,onEvent)
                }
            }
        }
    }
}

//converter for some values
@Composable
fun convertCondition(condition: Int = 0): String {
    if(condition == 1){
        return "Im Besitz"
    }
    if(condition == 2){
        return "Schon gelesen"
    }
    return  "Noch kaufen"
}
@Composable
fun convertRating(rating: Int = -1): String {
    if(0 <= rating && rating <= 10){
        return rating.toString()
    }
    return  ""
}

//components
@Composable
fun StatsItem(list: List<Book>, backgroundColor:androidx.compose.ui.graphics.Color){
    Row(
        modifier = Modifier
            .background(color =backgroundColor, RoundedCornerShape(7.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = list.size.toString(),
            color = Beige,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .background(color = backgroundColor, RoundedCornerShape(7.dp))
                .padding(5.dp,2.dp)
        )
    }
}
@Composable
fun FilterItem(icon: ImageVector, color: androidx.compose.ui.graphics.Color, description:String, onEvent:(BookEvent)->Unit,sortType: SortType){
    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = color,
        modifier = Modifier.clickable(onClick = { onEvent(BookEvent.SortBooks(sortType)) }))
}
@Composable
fun BookStatTemplate(text:String,icon: ImageVector,description: String){
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Darkgrey,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(5.dp,0.dp),
            color = Darkgrey,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
        )
    }
}
@Composable
fun MenuItem(icon: ImageVector, onEvent: Function0<Unit>){
    IconButton(
        onClick = onEvent,
        modifier = Modifier.background(color = Darkgrey, shape = RoundedCornerShape(20.dp)),
    )
    {
        Icon(
            imageVector = icon,
            contentDescription = "MenuItem",
            tint = Darkbeige)
    }
}
@Composable
fun Taskbar(state: BookState,onEvent: (BookEvent)-> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp, end = 10.dp,top=50.dp, bottom = 5.dp)
            .height(120.dp)
            .fillMaxSize()
            .background(
                color = LightBlue,
                shape = RoundedCornerShape(20))
            .border(5.dp, color = MiddeBlue, shape = RoundedCornerShape(20))
    )
    {
        //taskbar
        Box(
            modifier = Modifier
                .background(color = androidx.compose.ui.graphics.Color.Transparent)
                .padding(15.dp,15.dp,15.dp,0.dp)
        )
        {
            //searchField
            TextField(
                shape = RoundedCornerShape(10.dp),
                value = state.searchText,
                onValueChange = {
                    onEvent(BookEvent.OnSearchTextChange(it))
                },
                placeholder = {
                    Text(
                        text = "Buch suchen",
                        color = androidx.compose.ui.graphics.Color.Gray,
                        fontWeight = FontWeight.Bold)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        tint = androidx.compose.ui.graphics.Color.Gray,
                        contentDescription = "Search icon"
                    )
                },
                trailingIcon = {
                    // if the searchText is not blank then show the clear button
                    if(!state.searchText.isBlank())
                        IconButton(
                            onClick = {
                                onEvent(BookEvent.OnSearchTextChange(""))
                            }
                        )
                        {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                tint = androidx.compose.ui.graphics.Color.Gray,
                                contentDescription = "Search icon"
                            )
                        }
                },
                singleLine = true,
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth()
                    .padding(0.dp),
                colors = TextFieldDefaults.colors(unfocusedContainerColor = LightWithe, focusedContainerColor = LightWithe,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent, focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent),

                )
        }

        //filter + stats for the main list
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp,0.dp,20.dp,0.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            //when the main list is not empty then show the stats for the main list
            if(!state.books.isEmpty())
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                    ){
                    //show the amount of books in the main list that satisfied condition == 0
                    val condition0 = state.books.filter { book -> book.condition == 0 }
                    if(!condition0.isEmpty())
                        StatsItem(condition0,Darkbeige)

                    //show the amount of books in the main list that satisfied condition == 1
                    val condition1 = state.books.filter { book -> book.condition == 1 }
                    if(!condition1.isEmpty())
                        StatsItem(condition1,DarkRed)

                    //show the amount of books in the main list that satisfied condition == 2
                    val condition2 = state.books.filter { book -> book.condition == 2 }
                    if(!condition2.isEmpty())
                        StatsItem(condition2,Green)

                    //show the total amount of books in the main list
                    StatsItem(state.books,Darkblue)
                }
            }

            //filter for the main list
            Row (
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            )
            {
                SortType.entries.forEach { sortType ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    {
                        //set the color of icon
                        var iconColor = androidx.compose.ui.graphics.Color(Color.DKGRAY)
                        if(state.sortType == sortType)
                            iconColor = androidx.compose.ui.graphics.Color(Color.BLACK)

                        //items of the filter
                        if(sortType == SortType.TITLE)
                            FilterItem(Icons.Default.Edit,iconColor,"Title Filter",onEvent,sortType)
                        if(sortType == SortType.AUTHOR)
                            FilterItem(Icons.Default.Person,iconColor,"Author Filter",onEvent,sortType)
                        if(sortType == SortType.RATING)
                            FilterItem(Icons.Default.Star,iconColor,"Rating Filter",onEvent,sortType)
                        if(sortType == SortType.CONDITION)
                            FilterItem(Icons.Default.Info,iconColor,"Condition Filter",onEvent,sortType)
                        if(sortType == SortType.ID)
                            FilterItem(Icons.Default.DateRange,iconColor,"ID Filter",onEvent,sortType)
                    }
                }
            }
        }
    }
}
@Composable
fun ItemTemplate(book: Book, onEvent: (BookEvent) -> Unit) {
    //Variable for the item
    var expanded by remember { mutableStateOf(false) }

    //set the background color depend to the condition of the book
    var cardColor = Darkbeige
    if(book.condition == 1){
        cardColor = DarkRed
    }
    if(book.condition == 2) {
        cardColor = Green
    }

    //the item card for the book
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(color = Darkblue),
        shape = RoundedCornerShape(0.dp),
    ) {
        Column(
            modifier = Modifier
                .background(color = Darkblue),
        )
        {
            //title
            Text(
                text = book.title,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .align(alignment = Alignment.Start)
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 5.dp),
                color = Darkbeige,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )

            //body
            Card(
                shape = RoundedCornerShape(10,10,30,30),
                border = BorderStroke(width = 4.dp,color = Beige),
                modifier = Modifier
                    .padding(5.dp,0.dp)
                    .background(color = Darkblue)
                    .clickable(
                        onClick = {
                            expanded = !expanded
                        }
                    )
            )
            {
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = cardColor),
                    verticalAlignment = Alignment.CenterVertically,
                    )
                {

                    //the image of the book
                    Image(
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = "Photo of Book",
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .padding(10.dp)
                    )

                    //the other stats of the book
                    Column(
                        horizontalAlignment = AbsoluteAlignment.Left,
                        modifier = Modifier.padding(5.dp))
                    {

                        //if the author of the book is given then display it
                        if(!book.author.isBlank()) {
                            BookStatTemplate(book.author,Icons.Default.Person,"Author of the book")
                        }

                        Row {

                            //display the condition of the book
                            BookStatTemplate(convertCondition(book.condition),Icons.Default.Info,"Condition of the book")

                            //if the rating of the book is given then display it
                            if(convertRating(book.rating) != "") {
                                BookStatTemplate(convertRating(book.rating),Icons.Default.Star,"Rating of the book")
                            }
                        }
                    }
                }
            }

            //the menu
            if(expanded)
                Box(
                    modifier = Modifier
                        .padding(end = 20.dp, bottom = 0.dp, top = 5.dp)
                        .align(Alignment.End),
                )
                {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    )
                    {

                        //if a urlLink is given than show it
                        if(!book.urllink.isBlank())
                        {
                            MenuItem(Icons.Default.ShoppingCart,{onEvent(BookEvent.LoadURL(book.urllink))})
                        }

                        // change the book
                        MenuItem(Icons.Default.Settings,{onEvent(BookEvent.ChangeBook(book))})

                        //delete the book
                        MenuItem(Icons.Default.Delete,{onEvent(BookEvent.DeleteBook(book))})
                    }
                }
        }
    }
}