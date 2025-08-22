package com.example.koios

import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModel (
    //Access Point to the database
    private val dao: BookDao,
    //Traceback to the MainActivity
    var activity: MainActivity?
): ViewModel(){

    //Variable for the booklist
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _sortType = MutableStateFlow(SortType.CONDITION)
    private val _books = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                SortType.TITLE -> dao.getBooksOrderedByTitle()
                SortType.AUTHOR -> dao.getBooksOrderedByAuthor()
                SortType.RATING -> dao.getBooksOrderedByRating()
                SortType.CONDITION -> dao.getBooksOrderedByCondition()
                SortType.ID -> dao.getBooksOrderedById()
            }
        }
        .combine(searchText){ book, text ->
            if(text.isBlank()){
                book
            }else{
                book.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),emptyList())

    // Access point to the backend
    private val _state = MutableStateFlow(BookState())
    val state = combine(_state,_sortType,_books){state,sortType,books->
        state.copy(
            books = books,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BookState())


    //Event logic
    fun onEvent(event: BookEvent){
        when(event){
            is BookEvent.SetAuthor -> {
                _state.update { it.copy(
                    author = event.author
                ) }
            }
            is BookEvent.SetRating -> {
                _state.update { it.copy(
                    rating = event.rating
                ) }
            }
            is BookEvent.SetCondition -> {
                _state.update { it.copy(
                    condition = event.condition
                ) }
            }
            is BookEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }
            is BookEvent.SetURLLink -> {
                _state.update { it.copy(
                    urlLink = event.urllink
                ) }
            }
            is BookEvent.SetImage -> {
            }

            is BookEvent.HideDialog -> {
                setStateToDefault()
            }
            is BookEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingBook = true
                ) }
            }
            is BookEvent.SortBooks -> {
                _sortType.value = event.sortType
            }
            is BookEvent.DeleteBook -> {
                viewModelScope.launch {
                    dao.deleteBool(event.book)
                }

            }
            is BookEvent.SaveBook -> {
                save()
            }
            is BookEvent.ChangeBook ->{
                _state.update { it.copy(
                    isAddingBook = false,
                    isChangeBook = true,
                    title = event.changebook.title,
                    author = event.changebook.author,
                    rating = event.changebook.rating,
                    condition = event.changebook.condition,
                    image = event.changebook.image,
                    urlLink = event.changebook.urllink,
                    id = event.changebook.id
                ) }
            }
            is BookEvent.OnSearchTextChange ->{
                _state.update { it.copy(
                    searchText = event.searchText
                ) }
                _searchText.value = event.searchText
            }
            is BookEvent.LoadURL -> {
                activity?.openUrl(event.url)
            }
            is BookEvent.Expandel ->{
                _state.update { it.copy(
                    isMenuExpand =  event.exand
                ) }
            }
        }
    }

    //set the state values to default
    fun setStateToDefault()
    {
        _state.update { it.copy(
            isAddingBook = false,
            isChangeBook = false,
            title = "",
            author = "",
            rating = -1,
            condition = 0,
            image = "",
            urlLink = "",
            id = 0
        ) }
    }

    //save the book from the current state in the database and execute some commands
    fun save(){

        val title = state.value.title
        val author = state.value.author
        val rating = state.value.rating
        val condition = state.value.condition
        val image = state.value.image
        val urlLink = state.value.urlLink

        var book = Book(
            title = title,
            author = author,
            rating = rating,
            condition = condition,
            image = image,
            urllink = urlLink
        )

        if(state.value.isChangeBook)
        {
            book = Book(
                id = state.value.id,
                title = title,
                author = author,
                rating = rating,
                condition = condition,
                image = image,
                urllink = urlLink
            )
        }

        //execute commands
        //insert a decoded string to the database
        if(urlLink == "##insert##")
        {
            try {
                insert(title)
            }
            catch (e: Exception)
            {
                Toast.makeText(activity, "Somthing got wrong", Toast.LENGTH_LONG).show()
                return
            }
            setStateToDefault()
            return
        }

        //export a decoded file from the database
        if(urlLink == "##export##")
        {
            try {
                exportDatabase()
            }
            catch (e: Exception)
            {
                Toast.makeText(activity, "Somthing got wrong", Toast.LENGTH_LONG).show()
                return
            }
            setStateToDefault()
            return
        }

        // Has some problems with the permission
        //import a decoded file to the database
//        if(urlLink == "##import##")
//        {
//            importDatabase()
//            setStateToDefault()
//            return
//        }

        //delete all books from the database
        if(urlLink == "##delete##")
        {
            deleteAll()
            setStateToDefault()
            return
        }

        if (title.isBlank()){
            return
        }

        viewModelScope.launch {
            dao.upsetBook(book)
        }
        setStateToDefault()
    }

    //save many books from a decoded string
    fun insert(input: String){
        if(!input.isBlank()) {
            for(line in input.lines()){

                val content = line.split("#")

                if (content.size < 7){
                    continue
                }

                val book = Book(
                    id = content[0].toInt(),
                    title = content[1],
                    author = content[2],
                    urllink = content[3],
                    image = content[4],
                    rating = content[5].toInt(),
                    condition = content[6].toInt()
                )

                viewModelScope.launch {
                    dao.upsetBook(book)
                }
            }
        }
    }

    //export the current database to a external text file
    fun exportDatabase()
    {
        // decode the database
        val lines = ArrayList<String>()
        val separate = "#"
        onEvent(BookEvent.OnSearchTextChange(""))
        for(book in _books.value){

            val line = book.id.toString()+separate+book.title+separate+book.author+separate+book.urllink+separate+book.image+separate+book.rating.toString()+separate+book.condition.toString()
            lines += line
        }


        // set the file
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"KoiosBookList.txt")

        //write the file
        FileOutputStream(file).use { outputStream ->
            lines.forEach {
                outputStream.write("$it\n".encodeToByteArray())
            }
        }

        //set the permission
        file.setReadable(true)
        file.setWritable(true)

        //create the file
        file.createNewFile()

        Toast.makeText(activity, "Books are saved in Downloads as KoiosBookList.txt", Toast.LENGTH_LONG).show()
    }

    //import books to the current database from a external text file
    fun importDatabase()
    {
        // set the file
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"KoiosBookList.txt")

        if(!file.exists())
        {
            Toast.makeText(activity, "There is not file (KoiosBookList.txt) in Downloads", Toast.LENGTH_LONG).show()
            return
        }

        //read all lines from the file
        val data = emptyList<String>()

        try {
            //data = file.readLines()
            FileInputStream(file).use{input->
                val a = input.readAllBytes()
            }
        }
        catch (e: Exception)
        {
            Toast.makeText(activity, "Somthing got wrong"+e.message, Toast.LENGTH_LONG).show()
            return
        }



        // encode the file
        for(line in data){

            val content = line.split("#")
            val book = Book(
                id = content[0].toInt(),
                title = content[1],
                author = content[2],
                urllink = content[3],
                image = content[4],
                rating = content[5].toInt(),
                condition = content[6].toInt()
            )

            viewModelScope.launch {
                dao.upsetBook(book)
            }
        }

        Toast.makeText(activity, "Books are successful imported", Toast.LENGTH_LONG).show()
    }

    //delete all books from the database
    fun deleteAll(){
        onEvent(BookEvent.OnSearchTextChange(""))
        for(book in _books.value)
        {
            onEvent(BookEvent.DeleteBook(book))
        }
    }

}