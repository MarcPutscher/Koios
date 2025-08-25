package com.example.koios


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.koios.ui.theme.Card3BackgroundColor
import com.example.koios.ui.theme.DarkGrey
import com.example.koios.ui.theme.TaskBarBackgroundColor
import com.example.koios.ui.theme.TextFieldBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookDialog(state: BookState, onEvent: (BookEvent) -> Unit, modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = { onEvent(BookEvent.HideDialog) },
        containerColor = DarkGrey,
        title = {Title(state)},
        text = {

            //zooming the image
            if(state.isZooming)
                ImageDialog(state = state, onEvent = onEvent)

            //choose an image Url
            if(state.isImageChoose)
                ChooseImageDialog(state = state, onEvent = onEvent)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            )
            {
                //text input of title
                TextField(
                    value = state.title,
                    onValueChange = {
                        onEvent(BookEvent.SetTitle(it))
                    },
                    placeholder = {
                        Text(text = "Titel", color = Color.Gray,fontWeight = FontWeight.Bold)
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

                //text input of author
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
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = TextFieldBackgroundColor, focusedContainerColor = TextFieldBackgroundColor,
                        unfocusedIndicatorColor = Transparent, focusedIndicatorColor = Transparent),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            tint = Color.Gray,
                            contentDescription = ""
                        )
                    },
                    trailingIcon = {
                        // if the searchText is not blank then show the clear button
                        if(!state.author.isBlank())
                            IconButton(
                                onClick = {
                                    onEvent(BookEvent.SetAuthor(""))
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

                //text input of image
                TextField(
                    value = state.image,
                    onValueChange = {
                        onEvent(BookEvent.SetImage(it))
                    },
                    placeholder = {
                        Text(text = "Image", color = Color.Gray,fontWeight = FontWeight.Bold)
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = TextFieldBackgroundColor, focusedContainerColor = TextFieldBackgroundColor,
                        unfocusedIndicatorColor = Transparent, focusedIndicatorColor = Transparent),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.book),
                            contentDescription = "",
                            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                            modifier = Modifier.clickable(
                                onClick = {
                                    onEvent(BookEvent.GenearteImage)
                                }
                            )
                        )
                    },
                    trailingIcon = {
                        // if the searchText is not blank then show the clear button
                        if(!state.image.isBlank())
                            IconButton(
                                onClick = {
                                    onEvent(BookEvent.SetImage(""))
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

                //text input of urlLink and access point for commands
                TooltipBox(
                    modifier = modifier,
                    positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                    tooltip = {
                        RichTooltip(
                            title = { Text(text = "internal commands") }
                        ) {
                            Text(text = "##delete## --> remove all books" +
                                    "\n##insert## --> insert books in 'Titel' with the pattern 'id(int)#title(string)#author(string)#urlLink(string)#image#(string)rating(int)#condition(int)'\n" +
                                    //"##import## --> import all books from the Downloads folder in the 'KoiosBookList.txt' file with decode pattern 'id#title#author#urlLink#image#rating(int)#condition(int)'\n" +
                                    "##export## --> export all books to the file 'KoiosBookList.txt' in the folder Downloads with the same pattern as import\n" +
                                    "##image## -->try to set the images fro every book in the database")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    TextField(
                        value = state.urlLink,
                        onValueChange = {
                            onEvent(BookEvent.SetURLLink(it))
                        },
                        placeholder = {
                            Text(text = "URL", color = Color.Gray,fontWeight = FontWeight.Bold)
                        },
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = TextFieldBackgroundColor, focusedContainerColor = TextFieldBackgroundColor,
                            unfocusedIndicatorColor = Transparent, focusedIndicatorColor = Transparent),
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.ShoppingCart,
                                tint = Color.Gray,
                                contentDescription = ""
                            )
                        },
                        trailingIcon = {
                            // if the searchText is not blank then show the clear button
                            if(!state.urlLink.isBlank())
                                IconButton(
                                    onClick = {
                                        onEvent(BookEvent.SetURLLink(""))
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


                Spacer(Modifier.height(5.dp))

                //slider input of rating
                Column (
                    modifier = Modifier
                        .background(
                            color = TaskBarBackgroundColor,
                            shape = RoundedCornerShape(20))
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    //title
                    Text(
                        text = "Bewertung: "+ state.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    //content
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    //picker input of condition
                    Column(
                        modifier = Modifier
                            .padding(0.dp,10.dp,0.dp,0.dp)
                            .background(
                                color = TaskBarBackgroundColor,
                                shape = RoundedCornerShape(20)),
                        horizontalAlignment = Alignment.Start

                    )
                    {
                        //title
                        Text(
                            text = "Status",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 5.dp)
                        )

                        //content
                        LazyColumn(
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 15.dp)
                        )
                        {
                            items(items=listOf(0,1,2)){ conditionType ->
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                )
                                {
                                    RadioButton(
                                        selected = state.condition == conditionType,
                                        onClick = {
                                            onEvent(BookEvent.SetCondition(conditionType))
                                        }
                                    )

                                    //set text of the item
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

                    //image of the book
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                            onEvent(BookEvent.ZoomImage)
                        }),
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        if(!state.image.isBlank()){
                            SubcomposeAsyncImage(
                                model = state.image,
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(120.dp)
                                    .padding(start = 10.dp,0.dp,10.dp,0.dp),
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
                        else{
                            Image(
                                painter = painterResource(id = R.drawable.book),
                                contentDescription = "Photo of Book",
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(70.dp)
                                    .padding(0.dp)
                            )
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
@Composable
fun Title(state: BookState){
    //set the title of the dialog
    var title = "Buch hinzufügen"
    if(state.id != 0)
        title = "Buch ändern"
    Text(
        text = title,
        fontWeight = FontWeight.Black,
        fontSize = 30.sp,
        color = Card3BackgroundColor,
        modifier = Modifier
            .fillMaxWidth(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDialog(state: BookState, onEvent: (BookEvent) -> Unit)
{
    if(state.image.isBlank())
        return

    AlertDialog(
        onDismissRequest = { onEvent(BookEvent.EndZoomImage) },
        containerColor = Transparent,
        title = {},
        text = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //image of the book
                if(!state.image.isBlank()){
                    SubcomposeAsyncImage(
                        model = state.image,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .width(300.dp)
                            .height(500.dp)
                            .padding(start = 0.dp,0.dp,0.dp,0.dp)
                            .background(color = Card3BackgroundColor)
                            .align(Alignment.CenterHorizontally),
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.book),
                                contentDescription = "Photo of Book",
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(500.dp)
                            )
                        }
                    )
                }
                else{
                    Image(
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = "Photo of Book",
                        modifier = Modifier
                            .width(300.dp)
                            .height(500.dp)
                    )
                }

            }
        },
        confirmButton = {},
        modifier = Modifier
            .width(300.dp)
            .height(500.dp)
            .padding(0.dp)
            .clickable(
                onClick ={
                    onEvent(BookEvent.EndZoomImage)
                }
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseImageDialog(state: BookState, onEvent: (BookEvent) -> Unit)
{
    if(state.imageOption.isEmpty())
        return

    AlertDialog(
        onDismissRequest = { onEvent(BookEvent.ImageSelected("")) },
        containerColor = Transparent,
        title = {},
        text = {
            LazyColumn(
                Modifier.fadingEdge(Brush.verticalGradient(0.8f to Color.Black, 1f to Transparent)).background(Transparent)
            )
            {
                items(state.imageOption){ imageUrl->
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        //image of the book
                        if(!imageUrl.isBlank()){
                            SubcomposeAsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(300.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .clickable(
                                        onClick = {
                                            onEvent(BookEvent.ImageSelected(imageUrl))
                                        }
                                    ),
                                error = {
                                    Image(
                                        painter = painterResource(id = R.drawable.book),
                                        contentDescription = "Photo of Book",
                                        modifier = Modifier
                                            .width(200.dp)
                                            .height(300.dp)
                                    )
                                }
                            )
                        }

                        Spacer(Modifier.height(10.dp))
                    }
                }

                item {
                    Spacer(Modifier.height(50.dp))
                }
            }
        },
        confirmButton = {},
        modifier = Modifier
            .width(300.dp)
            .height(450.dp)
            .padding(0.dp)
    )
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }