package com.example.koios.view

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.koios.model.Book
import com.example.koios.model.BookEvent
import com.example.koios.model.BookState
import com.example.koios.types.DialogType
import com.example.koios.R
import com.example.koios.types.SortType
import com.example.koios.ui.theme.TextColor2
import com.example.koios.ui.theme.Card2BackgroundColor
import com.example.koios.ui.theme.Card3BackgroundColor
import com.example.koios.ui.theme.DarkBlue
import com.example.koios.ui.theme.DarkGrey
import com.example.koios.ui.theme.Card1BackgroundColor
import com.example.koios.ui.theme.DialogBackgroundColor
import com.example.koios.ui.theme.DialogButtonBackgroundColor
import com.example.koios.ui.theme.DialogButtonTextColor
import com.example.koios.ui.theme.DialogTextColor
import com.example.koios.ui.theme.HighLightBackground
import com.example.koios.ui.theme.TaskBarBackgroundColor
import com.example.koios.ui.theme.TextFieldBackgroundColor
import com.example.koios.ui.theme.HighLightText
import com.example.koios.ui.theme.LightGrey
import com.example.koios.ui.theme.MenuButtonBackgroundColor
import com.example.koios.ui.theme.MenuButtonIconColor
import com.example.koios.ui.theme.RobinEggBlue
import com.example.koios.ui.theme.TextColor1
import com.example.koios.ui.theme.makeColorDarker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(state: BookState, onEvent: (BookEvent) -> Unit){
    //Variable for the scroll to top function
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    //main layout
    Scaffold (
        floatingActionButton = {
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                //if the main list can scroll to the top then show this button
                if(listState.canScrollBackward)
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)} },
                        modifier = Modifier
                            .align(Alignment.End),
                        containerColor = MenuButtonBackgroundColor
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Add book",
                            Modifier.size(30.dp),
                            tint = MenuButtonIconColor
                        )
                    }

                if(state.isMenuOpen)
                {
                    //the button for download the images from the books
                    FloatingActionButton(
                        onClick = { onEvent(BookEvent.ShowDialog(DialogType.IMAGE)) },
                        containerColor = MenuButtonBackgroundColor
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_imagesmode_24),
                            contentDescription = "Image books",
                            Modifier.size(30.dp),
                            tint = MenuButtonIconColor
                        )
                    }

                    //the button for create a pdf from the books
                    FloatingActionButton(
                        onClick = { onEvent(BookEvent.ShowDialog(DialogType.PDF)) },
                        containerColor = MenuButtonBackgroundColor
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_picture_as_pdf_24),
                            contentDescription = "create a PDF from the books",
                            Modifier.size(30.dp),
                            tint = MenuButtonIconColor
                        )
                    }

                    //the button for exporting the database
                    FloatingActionButton(
                        onClick = { onEvent(BookEvent.ShowDialog(DialogType.EXPORT)) },
                        containerColor = MenuButtonBackgroundColor
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_upload_file_24),
                            contentDescription = "Export books",
                            modifier = Modifier.size(30.dp),
                            tint = MenuButtonIconColor
                        )
                    }

                    //the button for importing the database
                    FloatingActionButton(
                        onClick = { onEvent(BookEvent.ShowDialog(DialogType.IMPORT)) },
                        containerColor = MenuButtonBackgroundColor
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_sim_card_download_24),
                            contentDescription = "Import books",
                            Modifier.size(30.dp),
                            tint = MenuButtonIconColor
                        )
                    }

                    //the button for insert books
                    FloatingActionButton(
                        onClick = { onEvent(BookEvent.ShowDialog(DialogType.INSERT)) },
                        containerColor = MenuButtonBackgroundColor
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_library_add_24),
                            contentDescription = "Add many books",
                            Modifier.size(30.dp),
                            tint = MenuButtonIconColor
                        )
                    }

                    //the button for delete all books
                    FloatingActionButton(
                        onClick = { onEvent(BookEvent.ShowDialog(DialogType.DELETE)) },
                        containerColor = MenuButtonBackgroundColor
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete books",
                            Modifier.size(30.dp),
                            tint = MenuButtonIconColor
                        )
                    }

                    //the button for adding a new book
                    FloatingActionButton(
                        onClick = { onEvent(BookEvent.ShowDialog(DialogType.ADD)) },
                        containerColor = MenuButtonBackgroundColor
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add book",
                            Modifier.size(30.dp),
                            tint = MenuButtonIconColor
                        )
                    }
                }

                //the button for the menu
                FloatingActionButton(
                    onClick = { onEvent(BookEvent.ToggleMenu) },
                    containerColor = MenuButtonBackgroundColor
                )
                {
                    var icon = Icons.Default.MoreVert
                    if(state.isMenuOpen)
                        icon = Icons.Default.KeyboardArrowDown
                    Icon(
                        imageVector = icon,
                        contentDescription = "Add book",
                        Modifier.size(30.dp),
                        tint = MenuButtonIconColor
                    )
                }
            }
        },
        modifier = Modifier.padding(0.dp)
    )
    { padding->
        //show the dialog from the specific menu option
        if(state.showDialog)
            Dialog(state,onEvent)

        //zooming the image
        if(state.isZooming)
        {
            ImageDialog(state = state, onEvent = onEvent)
        }

        //layout for the taskbar and the main list
        Column(
            modifier = Modifier
                .background(color = DarkBlue)
                .fillMaxSize()
                .padding(padding)
        )
        {

            //taskbar
            Taskbar(state, onEvent)

            if(state.isLoading)
            {
                Box(
                    modifier = Modifier.fillMaxSize()
                )
                {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = RobinEggBlue
                    )
                }
            }
            else {
                //if the list is empty then show this image
                if (state.books.isEmpty())
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_search_off_24),
                            contentDescription = "Condition Filter",
                            modifier = Modifier
                                .size(300.dp)
                                .fillMaxSize()
                                .align(Alignment.Center)

                        )
                    }


                //main list where the books are displayed
                LazyColumn(
                    state = listState,
                )
                {
                    items(state.books) { book ->
                        ItemTemplate(book, onEvent)
                    }

                    //make space to interact better with the last item
                    item {
                        Spacer(Modifier.height(200.dp))
                    }
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
fun Dialog(state: BookState, onEvent: (BookEvent) -> Unit){

    if(state.dialogType == DialogType.ADD || state.dialogType == DialogType.CHANGE){
        AddBookDialog(state, onEvent)
    }

    if(state.dialogType == DialogType.DELETE)
    {
        DefaultDialog(
            "Bücher löschen",
            "Möchten Sie wirklich alle Bücher löschen?",
            { onEvent(BookEvent.HideDialog) },
            { onEvent(BookEvent.DeleteBooks) },
            height = 200.dp
        )
    }

    if(state.dialogType == DialogType.INSERT)
    {
        AlertDialog(
            onDismissRequest = { onEvent(BookEvent.HideDialog) },
            containerColor = DialogBackgroundColor,
            title = {
                Text(
                    text = "Bücher manuel hinzufügen",
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    color = Card3BackgroundColor,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            },
            text = {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                )
                {
                    Text(
                        text = "Die Bücher müssen decodiert Zeilenweise eingegeben werden.\n\n" +
                                "Patter: id(int)#title(string)#author(string)#\nurlLink(string)#image(string)#rating(int)\n#condition(int)",
                        color = DialogTextColor,
                    )
                    //text input
                    TextField(
                        value = state.title,
                        onValueChange = {onEvent(BookEvent.SetTitle(it))},
                        placeholder = {
                            Text(text = "Input", color = Color.Gray,fontWeight = FontWeight.Bold)
                        },
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = TextFieldBackgroundColor, focusedContainerColor = TextFieldBackgroundColor,
                            unfocusedIndicatorColor = Transparent, focusedIndicatorColor = Transparent),
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                tint = Color.Gray,
                                contentDescription = ""
                            )
                        },
                        trailingIcon = {
                            // if the searchText is not blank then show the clear button
                            if(!state.title.isBlank())
                                IconButton(
                                    onClick = {
                                        onEvent(BookEvent.SetTitle(""))
                                    }
                                )
                                {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        tint = Color.Gray,
                                        contentDescription = ""
                                    )
                                }
                        },
                    )
                }
            },
            confirmButton = {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                )
                {
                    //dismiss
                    Button(
                        onClick = { onEvent(BookEvent.HideDialog) } ,
                        colors = ButtonColors(
                            DialogButtonBackgroundColor,
                            contentColor = DialogButtonBackgroundColor,
                            disabledContainerColor = makeColorDarker(DialogButtonBackgroundColor,1.9),
                            disabledContentColor = makeColorDarker(DialogButtonBackgroundColor,1.9)
                        )
                    )
                    {
                        Text(text = "Abbrechen", color = DialogButtonTextColor)
                    }

                    //accept
                    Button(onClick = { onEvent(BookEvent.InsertManyBooks(state.title)) } ,
                        colors = ButtonColors(
                            DialogButtonBackgroundColor,
                            contentColor = DialogButtonBackgroundColor,
                            disabledContainerColor = makeColorDarker(DialogButtonBackgroundColor,1.9),
                            disabledContentColor = makeColorDarker(DialogButtonBackgroundColor,1.9)
                        ))
                    {
                        Text(text = "Hinzufügen", color = DialogButtonTextColor)
                    }
                }
            },
            modifier = Modifier
                .width(320.dp)
                .height(400.dp)
        )
    }

    if(state.dialogType == DialogType.IMPORT)
    {
        DefaultDialog(
            "Bücher importieren",
            "Möchten Sie wirklich die Bücher aus der Datei KoiosBookList.txt im Ordner Documents/Koios importieren?",
            { onEvent(BookEvent.HideDialog) },
            { onEvent(BookEvent.ImportBooks) }
        )
    }

    if(state.dialogType == DialogType.EXPORT)
    {
        DefaultDialog(
            "Bücher exportieren",
            "Möchten Sie wirklich die alle Bücher in Datei KoiosBookList.txt im Ordner Documents/Koios exportieren?",
            { onEvent(BookEvent.HideDialog) },
            { onEvent(BookEvent.ExportBooks) }
        )
    }

    if(state.dialogType == DialogType.IMAGE)
    {
        DefaultDialog(
            "Bilder downloaden",
            "Möchten Sie wirklich für alle Bücher die Bilder in Documents/Koios/Images downloaden?",
            { onEvent(BookEvent.HideDialog) },
            { onEvent(BookEvent.ImageBooks) }
        )
    }

    if(state.dialogType == DialogType.METADATA)
    {
        DefaultDialog(
            "Metadaten",
            "Titel = ${state.title}\n" +
                    "Author = ${state.author}\n" +
                    "Bewertung = ${convertRating(state.rating)}\n" +
                    "Status = ${convertCondition(state.condition)}\n" +
                    "ImageURL = ${state.image}\n" +
                    "URL-Link = ${state.urlLink}\n\n" +
                    "ID = ${state.id}\n" +
                    "Current Image = ${state.currentImage}\n" +
                    "Image Path = ${state.imagePath}\n",
            dismiss = { onEvent(BookEvent.HideDialog) },
            confirm =null,
            height = 550.dp
        )
    }

    if(state.dialogType == DialogType.PDF)
    {
        AlertDialog(
            onDismissRequest = { onEvent(BookEvent.HideDialog) },
            containerColor = DialogBackgroundColor,
            title = {
                Text(
                    text = "Bücherliste als PDf",
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    color = Card3BackgroundColor,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            },
            text = {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                )
                {
                    //text for the dialog
                    Text(
                        text = "Möchten Sie wirklich eine PDF von den Büchern in der Datei KoiosBookList.pdf im Ordner Documents/Koios erstellen?",
                        color = DialogTextColor,
                    )

                    //picker input of filter
                    Column(
                        modifier = Modifier
                            .padding(50.dp, 10.dp, 50.dp, 0.dp)
                            .background(
                                color = DialogButtonBackgroundColor,
                                shape = RoundedCornerShape(20)
                            )
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        //title
                        Text(
                            text = "Filter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = DialogTextColor,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 5.dp)
                        )

                        //content
                        LazyRow(
                            modifier = Modifier
                                .padding(vertical = 0.dp, horizontal = 15.dp)
                                .height(100.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            items(items=listOf(0,1,2)){ filterIndex ->

                                var color = makeColorDarker(LightGrey,0.4)
                                if(state.booksFilter[filterIndex].toString()  == "t")
                                    color = DialogButtonTextColor

                                Box(
                                    modifier = Modifier
                                        .border(
                                            2.dp,
                                            color = makeColorDarker(color, 1.1),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(5.dp)
                                )
                                {
                                    var icon = ImageVector.vectorResource(R.drawable.outline_shopping_cart_24)
                                    if(filterIndex == 1)
                                        icon = ImageVector.vectorResource(R.drawable.outline_shelves_24)
                                    if(filterIndex == 2)
                                        icon = ImageVector.vectorResource(R.drawable.outline_menu_book_24)
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = "",
                                        tint = color,
                                        modifier = Modifier.clickable(onClick =
                                            {
                                                var newFilter = "ttt"
                                                if(state.booksFilter[filterIndex].toString() == "t")
                                                    newFilter = state.booksFilter.substring(0, filterIndex) + "f" + state.booksFilter.substring(filterIndex + 1)
                                                else
                                                    newFilter = state.booksFilter.substring(0, filterIndex) + "t" + state.booksFilter.substring(filterIndex + 1)
                                                onEvent(BookEvent.SetBookListFilter(newFilter))
                                            }
                                        ))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                )
                {
                    //dismiss
                    Button(
                        onClick = { onEvent(BookEvent.HideDialog) } ,
                        colors = ButtonColors(
                            DialogButtonBackgroundColor,
                            contentColor = DialogButtonBackgroundColor,
                            disabledContainerColor = makeColorDarker(DialogButtonBackgroundColor,1.9),
                            disabledContentColor = makeColorDarker(DialogButtonBackgroundColor,1.9)
                        )
                    )
                    {
                        Text(text = "Abbrechen", color = DialogButtonTextColor)
                    }

                    //accept
                    Button(onClick = { onEvent(BookEvent.PDFBooks(state.booksFilter)) } ,
                        colors = ButtonColors(
                            DialogButtonBackgroundColor,
                            contentColor = DialogButtonBackgroundColor,
                            disabledContainerColor = makeColorDarker(DialogButtonBackgroundColor,1.9),
                            disabledContentColor = makeColorDarker(DialogButtonBackgroundColor,1.9)
                        ))
                    {
                        Text(text = "Erstellen", color = DialogButtonTextColor)
                    }
                }
            },
            modifier = Modifier
                .width(320.dp)
                .height(330.dp)
        )
    }
}
@Composable
fun DefaultDialog(title: String, text: String, dismiss: (() -> Unit), confirm: (() -> Unit)?, height: Dp = 230.dp, accept: String = "Ja"){
        AlertDialog(
            onDismissRequest = dismiss ,
            containerColor = DialogBackgroundColor,
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    color = DialogTextColor,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            },
            text = {
                Text(
                    text = text,
                    color = DialogTextColor,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = rememberScrollState())
                )
            },
            confirmButton = {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                )
                {
                    if (confirm != null)
                    {
                        //dismiss
                        Button(
                            onClick = dismiss ,
                            colors = ButtonColors(
                                DialogButtonBackgroundColor,
                                contentColor = DialogButtonBackgroundColor,
                                disabledContainerColor = makeColorDarker(DialogButtonBackgroundColor,1.9),
                                disabledContentColor = makeColorDarker(DialogButtonBackgroundColor,1.9)
                            )
                        )
                        {
                            Text(text = "Abbrechen", color = DialogButtonTextColor)
                        }

                        //accept
                        Button(onClick = confirm ,
                            colors = ButtonColors(
                                DialogButtonBackgroundColor,
                                contentColor = DialogButtonBackgroundColor,
                                disabledContainerColor = makeColorDarker(DialogButtonBackgroundColor,1.9),
                                disabledContentColor = makeColorDarker(DialogButtonBackgroundColor,1.9)
                            ))
                        {
                            Text(text = accept, color = DialogButtonTextColor)
                        }
                    }
                    else
                        //dismiss
                        Button(
                            onClick = dismiss ,
                            colors = ButtonColors(
                                DialogButtonBackgroundColor,
                                contentColor = DialogButtonBackgroundColor,
                                disabledContainerColor = makeColorDarker(DialogButtonBackgroundColor,1.9),
                                disabledContentColor = makeColorDarker(DialogButtonBackgroundColor,1.9)
                            )
                        )
                        {
                            Text(text = "Schließen", color = DialogButtonTextColor)
                        }
                }
            },
            modifier = Modifier
                .width(320.dp)
                .height(height)
        )
}
@Composable
fun StatsItem(list: List<Book>, backgroundColor: Color, textColor: Color = TextColor2){
    Row(
        modifier = Modifier
            .background(color =backgroundColor, RoundedCornerShape(7.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = list.size.toString(),
            color = textColor,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .background(color = backgroundColor, RoundedCornerShape(7.dp))
                .padding(5.dp, 2.dp)
        )
    }
}
@Composable
fun FilterItem(icon: ImageVector, color: Color, description:String, onEvent:(BookEvent)->Unit, sortType: SortType){
    Box(
        modifier = Modifier
            .border(2.dp, color = makeColorDarker(color, 1.1), shape = RoundedCornerShape(10.dp))
            .padding(2.dp)
    )
    {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = color,
            modifier = Modifier.clickable(onClick = { onEvent(BookEvent.SortBooks(sortType)) }))
    }
}
@Composable
fun BookStatTemplate(text:String,icon: ImageVector,description: String,searchMatch: Int = 0){
    var backgroundColor = Transparent
    var textColor = DarkGrey
    if (searchMatch == 1) {
        backgroundColor = HighLightBackground
        textColor = HighLightText
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = DarkGrey,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(5.dp, 0.dp)
                .background(color = backgroundColor, shape = RoundedCornerShape(5.dp))
                .padding(2.dp),
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
        )
    }
}
@Composable
fun MenuItem(icon: ImageVector, onEvent: Function0<Unit>){
    IconButton(
        onClick = onEvent,
        modifier = Modifier.background(color = DarkGrey, shape = RoundedCornerShape(20.dp)),
    )
    {
        Icon(
            imageVector = icon,
            contentDescription = "MenuItem",
            tint = Card3BackgroundColor)
    }
}
@Composable
fun Taskbar(state: BookState, onEvent: (BookEvent)-> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp, end = 10.dp, top = 20.dp, bottom = 5.dp)
            .height(120.dp)
            .fillMaxSize()
            .background(
                color = TaskBarBackgroundColor,
                shape = RoundedCornerShape(20)
            )
            .border(
                5.dp,
                color = makeColorDarker(TaskBarBackgroundColor, 0.6),
                shape = RoundedCornerShape(20)
            )
    )
    {
        //taskbar
        Box(
            modifier = Modifier
                .background(color = Transparent)
                .padding(15.dp, 15.dp, 15.dp, 0.dp)
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
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        tint = Color.Gray,
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
                                tint = Color.Gray,
                                contentDescription = "Search icon"
                            )
                        }
                },
                singleLine = true,
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth()
                    .padding(0.dp),
                colors = TextFieldDefaults.colors(unfocusedContainerColor = TextFieldBackgroundColor, focusedContainerColor = TextFieldBackgroundColor,
                    unfocusedIndicatorColor = Transparent, focusedIndicatorColor = Transparent),

                )
        }

        //filter + stats for the main list
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            //when the main list is not empty then show the stats for the main list
            if(!state.books.isEmpty())
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                    )
                {
                    //show the amount of books in the main list that satisfied condition == 0
                    val condition0 = state.books.filter { book -> book.condition == 0 }
                    if(!condition0.isEmpty())
                        StatsItem(condition0,Card3BackgroundColor)

                    //show the amount of books in the main list that satisfied condition == 1
                    val condition1 = state.books.filter { book -> book.condition == 1 }
                    if(!condition1.isEmpty())
                        StatsItem(condition1, Card2BackgroundColor)


                    //show the amount of books in the main list that satisfied condition == 2
                    val condition2 = state.books.filter { book -> book.condition == 2 }
                    if(!condition2.isEmpty())
                        StatsItem(condition2, Card1BackgroundColor)


                    //show the total amount of books in the main list
                    Text(text = "=", color = TextColor1)
                    StatsItem(state.books,DarkBlue,  makeColorDarker(TextColor2,1.7))
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
                        var iconColor = makeColorDarker(TextColor1,1.7)
                        if(state.sortType == sortType)
                            iconColor = TextColor1

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
    var cardColor = Card3BackgroundColor
    if(book.condition == 1){
        cardColor = Card2BackgroundColor
    }
    if(book.condition == 2) {
        cardColor = Card1BackgroundColor
    }

    //the item card for the book
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(color = Transparent),
        shape = RoundedCornerShape(0.dp),
    )
    {
        Column(
            modifier = Modifier
                .background(color = DarkBlue),
        )
        {
            //title
            var backgroundColorText = Transparent
            var textColor = Card3BackgroundColor
            if (book.titleMatch == 1)
            {
                backgroundColorText = HighLightBackground
                textColor = HighLightText
            }
            Text(
                text = book.title,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .align(alignment = Alignment.Start)
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 5.dp)
                    .background(color = backgroundColorText, shape = RoundedCornerShape(5.dp))
                    .padding(2.dp),
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )

            //body
            Card(
                shape = RoundedCornerShape(10,10,30,30),
                border = BorderStroke(width = 4.dp,color = makeColorDarker(cardColor)),
                modifier = Modifier
                    .padding(5.dp, 0.dp)
                    .background(color = Transparent)
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
                    if(!book.image.isBlank()){
                        SubcomposeAsyncImage(
                            model = book.imagePath,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .width(70.dp)
                                .height(80.dp)
                                .padding(start = 20.dp, 10.dp, 10.dp, 10.dp)
                                .clickable(
                                    onClick = {
                                        onEvent(BookEvent.SetImage(book.imagePath))
                                        onEvent(BookEvent.ZoomImage)
                                    }
                                ),
                            error = {
                                SubcomposeAsyncImage(
                                    model = book.image,
                                    contentDescription = null,
                                    contentScale = ContentScale.FillHeight,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable(
                                            onClick = {
                                                onEvent(BookEvent.SetImage(book.image))
                                                onEvent(BookEvent.ZoomImage)
                                            }
                                        ),
                                    error = {
                                        Image(
                                            painter = painterResource(id = R.drawable.book),
                                            contentDescription = "Photo of Book",
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(80.dp)
                                                .padding(0.dp)
                                        )
                                    }
                                )
                            }
                        )
                    }
                    else{
                        Image(
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = "Photo of Book",
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .padding(10.dp)
                        )
                    }

                    //the other stats of the book
                    Column(
                        horizontalAlignment = AbsoluteAlignment.Left,
                        modifier = Modifier.padding(5.dp))
                    {

                        //if the author of the book is given then display it
                        if(!book.author.isBlank()) {
                            BookStatTemplate(book.author,Icons.Default.Person,"Author of the book",book.authorMatch)
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
                        .padding(end = 20.dp, bottom = 0.dp, top = 10.dp)
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

                        //metadata of the book
                        MenuItem(Icons.Default.Info,{onEvent(BookEvent.ShowMetadata(book))})
                    }
                }
        }
    }
}