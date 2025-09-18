package com.example.koios.viewmodel

import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koios.model.Book
import com.example.koios.service.BookDao
import com.example.koios.model.BookEvent
import com.example.koios.model.BookState
import com.example.koios.types.DialogType
import com.example.koios.model.Downloader
import com.example.koios.MainActivity
import com.example.koios.types.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModel (
    //Access Point to the database
    private val dao: BookDao,
    //Traceback to the MainActivity
    var activity: MainActivity?,
    var downloader: Downloader?
): ViewModel()
{

    //Variable for the booklist
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _sortType = MutableStateFlow(SortType.CONDITION)
    @OptIn(FlowPreview::class)
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
            book.forEach { it.removeMarks() }
            if(text.isBlank()){
                book
            }else{
                book.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _state.update { it.copy(isLoading = true) } }
        .debounce { 500L }
        .onEach { _state.update { it.copy(isLoading = false) } }
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000),emptyList())

    // Access point to the backend
    private val _state = MutableStateFlow(BookState())
    val state = combine(_state,_sortType,_books){state,sortType,books->
        state.copy(
            books = books,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), BookState())



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
                if(event.urlLink == "##search##")
                    getURLLink()
                else
                    _state.update { it.copy(
                        urlLink = event.urlLink
                    ) }
            }
            is BookEvent.SetImage -> {
                _state.update { it.copy(
                    image = event.image
                ) }
            }

            is BookEvent.HideDialog -> {
                setStateToDefault()
            }
            is BookEvent.ShowDialog -> {
                when(event.dialogType){
                    DialogType.ADD -> {
                        _state.update { it.copy(
                            isAddingBook = true,
                            showDialog = true,
                            dialogType = DialogType.ADD
                        ) }
                    }
                    DialogType.DELETE -> {
                        _state.update { it.copy(
                            showDialog = true,
                            dialogType = DialogType.DELETE
                        ) }
                    }
                    DialogType.INSERT -> {
                        _state.update { it.copy(
                            showDialog = true,
                            dialogType = DialogType.INSERT
                        ) }
                    }
                    DialogType.CHANGE -> {}
                    DialogType.IMPORT -> {
                        _state.update { it.copy(
                            showDialog = true,
                            dialogType = DialogType.IMPORT
                        ) }
                    }
                    DialogType.EXPORT -> {
                        _state.update { it.copy(
                            showDialog = true,
                            dialogType = DialogType.EXPORT
                        ) }
                    }
                    DialogType.IMAGE -> {
                        _state.update { it.copy(
                            showDialog = true,
                            dialogType = DialogType.IMAGE
                        ) }
                    }
                    DialogType.METADATA -> {
                        _state.update { it.copy(
                            showDialog = true,
                            dialogType = DialogType.METADATA
                        ) }
                    }
                }
            }
            is BookEvent.SortBooks -> {
                _sortType.value = event.sortType
            }
            is BookEvent.DeleteBook -> {
                viewModelScope.launch {
                    //delete the image from the file
                    val dirname = "Koios/Images/${event.book.title}${event.book.author}"
                    val dirpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path + "/"+dirname
                    if(File(dirpath).exists())
                    {
                        if (File(dirpath).isDirectory())
                            for (child in File(dirpath).listFiles()!!) {
                                child.delete()
                            }
                        File(dirpath).delete()
                    }

                    dao.deleteBool(event.book)
                }
            }
            is BookEvent.SaveBook -> {
                save()
            }
            is BookEvent.ChangeBook ->{
                _state.update { it.copy(
                    isChangeBook = true,
                    showDialog = true,
                    dialogType = DialogType.CHANGE,
                    title = event.changeBook.title,
                    author = event.changeBook.author,
                    rating = event.changeBook.rating,
                    condition = event.changeBook.condition,
                    image = event.changeBook.image,
                    urlLink = event.changeBook.urllink,
                    id = event.changeBook.id,
                    currentImage = event.changeBook.currentimage,
                    imagePath = event.changeBook.imagePath
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
            is BookEvent.GenerateImage ->{
                if(state.value.title.isBlank())
                    return

                viewModelScope.launch {
                    val imageUrl : List<String> = getImage(state.value.title, state.value.author,true)

                    if (!imageUrl.isEmpty())
                        _state.update { it.copy(
                            imageOption = imageUrl,
                            isImageChoose = true
                        ) }
                }

            }
            is BookEvent.ZoomImage -> {
                _state.update { it.copy(
                    isZooming = true
                ) }
            }
            is BookEvent.EndZoomImage -> {
                _state.update { it.copy(
                    isZooming = false
                ) }
            }
            is BookEvent.ImageSelected ->{
                _state.update { it.copy(
                    isImageChoose = false
                ) }
                onEvent(BookEvent.SetImage(event.image))
            }
            is BookEvent.ToggleMenu ->{
                _state.update { it.copy(
                    isMenuOpen = !_state.value.isMenuOpen
                ) }
            }
            is BookEvent.DeleteBooks -> {
                deleteAll()
                setStateToDefault()
            }
            is BookEvent.InsertManyBooks -> {
                insert(event.input)
                setStateToDefault()
            }
            is BookEvent.ExportBooks -> {
                exportDatabase()
                setStateToDefault()
            }
            is BookEvent.ImportBooks -> {
                importDatabase()
                setStateToDefault()
            }
            is BookEvent.ImageBooks ->{
                getImageAll()
                setStateToDefault()
            }
            is BookEvent.ShowMetadata ->{
                _state.update { it.copy(
                    title = event.currentBook.title,
                    author = event.currentBook.author,
                    rating = event.currentBook.rating,
                    condition = event.currentBook.condition,
                    image = event.currentBook.image,
                    urlLink = event.currentBook.urllink,
                    id = event.currentBook.id,
                    currentImage = event.currentBook.currentimage,
                    imagePath = event.currentBook.imagePath
                ) }
                onEvent(BookEvent.ShowDialog(DialogType.METADATA))
            }
        }
    }


    //set the state values to default
    fun setStateToDefault()
    {
        _state.update { it.copy(
            title = "",
            author = "",
            rating = -1,
            condition = 0,
            image = "",
            urlLink = "",
            id = 0,

            currentImage = "",
            imagePath = "",
            imageOption = emptyList(),
            dialogType = DialogType.ADD,

            isImageChoose = false,
            isZooming = false,
            isAddingBook = false,
            isChangeBook = false,
            isMenuOpen = false,
            showDialog = false,
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
        val currentImage = state.value.currentImage
        val imagePath = state.value.imagePath

        var book = Book(
            title = title,
            author = author,
            rating = rating,
            condition = condition,
            image = image,
            urllink = urlLink,
            currentimage = currentImage,
            imagePath = imagePath
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
                urllink = urlLink,
                currentimage = currentImage,
                imagePath = imagePath
            )
        }

        if (title.isBlank())
            return

        viewModelScope.launch {

            storeImage(book)

            dao.upsetBook(book)
        }
        setStateToDefault()
    }

    //save many books from a decoded string
    fun insert(input: String)
    {
        try {
            if(!input.isBlank()) {
                for(line in input.lines()){
                    try {
                        val content = line.split("#")

                        val book = Book(
                            id = content[0].toInt(),
                            title = content[1],
                            author = content[2],
                            urllink = content[3],
                            image = content[4],
                            rating = content[5].toInt(),
                            condition = content[6].toInt(),
                        )

                        viewModelScope.launch {
                            dao.upsetBook(book)
                        }
                    }
                    catch (e: Exception)
                    {
                        Toast.makeText(activity, "Somthing got wrong", Toast.LENGTH_LONG).show()
                        continue
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Toast.makeText(activity, "Somthing got wrong", Toast.LENGTH_LONG).show()
        }
    }

    //export the current database to a external text file
    fun exportDatabase()
    {
        try {
            // decode the database
            val lines = ArrayList<String>()
            val separate = "#"
            onEvent(BookEvent.OnSearchTextChange(""))
            for(book in _books.value){

                val line = book.id.toString()+separate+book.title+separate+book.author+separate+book.urllink+separate+book.image+separate+book.rating.toString()+separate+book.condition.toString()
                lines += line
            }

            //set file directory and file
            val fileDirName: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path +"/Koios"
            val fileName = "KoiosBookList.txt"

            if(!File(fileDirName).exists())
                File(fileDirName).mkdir()

            if(File(fileDirName, fileName).exists())
                File(fileDirName, fileName).delete()

            val file = File(fileDirName, fileName)

            //write the file
            FileOutputStream(file).use { outputStream ->
                lines.forEach {
                    outputStream.write("$it\n".encodeToByteArray())
                }
            }

            //create the file
            file.createNewFile()

            Toast.makeText(activity, "Books are saved in Documents/Koios as KoiosBookList.txt", Toast.LENGTH_LONG).show()
        }
        catch (e: Exception)
        {
            Toast.makeText(activity, "Somthing got wrong", Toast.LENGTH_LONG).show()
        }
    }

    //import books to the current database from a external text file
    fun importDatabase()
    {
        try {
            // set the file and directory
            val fileDir: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path +"/Koios"
            val file = File(fileDir, "KoiosBookList.TXT")


            if(!file.exists())
            {
                Toast.makeText(activity, "There is not file (KoiosBookList.txt) in Documents/Koios", Toast.LENGTH_LONG).show()
                return
            }

            //val data = file.readLines()
            val data = file.readLines()

            viewModelScope.launch {
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

                    storeImage(book)

                    dao.upsetBook(book)
                }
            }
        }
        catch (e: Exception)
        {
            Toast.makeText(activity, "Somthing got wrong", Toast.LENGTH_LONG).show()
            return
        }


        Toast.makeText(activity, "Books are successful imported", Toast.LENGTH_LONG).show()
    }

    //delete all books from the database
    fun deleteAll(){
        _state.update { it.copy(isLoading = true) }
        onEvent(BookEvent.OnSearchTextChange(""))
        for(book in _books.value)
        {
            onEvent(BookEvent.DeleteBook(book))
        }
        _state.update { it.copy(isLoading = false) }
    }

    //try to set the images from the books in the database
    fun getImageAll()
    {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            for(book in _books.value)
            {

                if(book.image == "null")
                    book.image = ""

                if(book.image.isBlank())
                {
                    val imageUrls = getImage(book.title, book.author)

                    if(!imageUrls.isEmpty())
                        book.image = imageUrls.first{url-> !url.isBlank()}

                    if(book.image == "null")
                        book.image = ""
                }

                storeImage(book)

                dao.upsetBook(book)
            }
        }
        _state.update { it.copy(isLoading = false) }
    }

    //store the image on device
    fun storeImage(book: Book)
    {
        if(book.image == book.currentimage)
            return

        val dirname = "Koios/Images/${book.title}${book.author}"
        val dirpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path + "/"+dirname
        val imagepath = "$dirpath/image.jpg"

        if(File(imagepath).exists() and book.imagePath.isBlank())
        {
            book.imagePath = imagepath
            book.currentimage = book.image
            return
        }

        if(!book.image.isBlank())
        {
            if(!book.imagePath.isBlank() and File(book.imagePath).exists()){
                File(book.imagePath).delete()
            }

            downloader?.downloadFile(book.image,book.title,dirname)
            book.imagePath = imagepath
        }
        book.currentimage = book.image
    }

    //get an image url from the internet with the title and the author as buzzwords
    suspend fun getImage(title:String, author:String,moreImage: Boolean = false): List<String> {
        val client = OkHttpClient()

        val imageUrls: MutableList<String> = mutableListOf()

        try {
            if(!moreImage)
            {
                val url = findBookCoverUrl(client, title,author)
                if(url != null)
                    imageUrls.add(url)
            }
            else{
                val urls = findBookCoverUrls(client,title,author)
                imageUrls.addAll(urls.filter { it != "null" })
            }
        }
        catch (e: Exception){
            e.message
        }

        try {

            val imageUrl = findGoogleBookCoverUrl(client,title,author).toString()
            imageUrls.add( imageUrl.replace("http://", "https://"))
        }
        catch (e: Exception){
            e.message
        }

        if(imageUrls.all { it == "" })
        {
            Toast.makeText(activity, "No image found\nfor $title", Toast.LENGTH_LONG).show()
            return emptyList()
        }

        return imageUrls
    }

    //get a Amazon url from the internet with the title and the author as buzzwords
    fun getURLLink(){
        val result = findAmazonURL(state.value.title,state.value.author)

        _state.update { it.copy(urlLink = result) }
    }

    // serialize the body of the internet page
    @Serializable
    data class OpenLibrarySearchResponse(
        val docs: List<Doc>
    )
    {
        @Serializable
        data class Doc(
            @SerialName("cover_i")
            val coverId: Int? = null,
            val title: String? = null,
            val authorName: List<String>? = null
        )
    }

    // Helper extension to URL-encode query parameters
    fun String.encodeUrl(): String = URLEncoder.encode(this, "UTF-8")

    //fetch a url from openlibrary.org
    suspend fun findBookCoverUrl(client: OkHttpClient, title: String, author: String): String? =
        withContext(
            Dispatchers.IO
        )
        {
            var url =
                "https://openlibrary.org/search.json?title=${title.encodeUrl()}&author=${author.encodeUrl()}&limit=5"
            if (author.isBlank())
                url = "https://openlibrary.org/search.json?title=${title.encodeUrl()}&limit=5"
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("HTTP request failed with code ${response.code}")
                    return@withContext ""
                }

                val body = response.body?.string() ?: return@withContext ""

                val json = Json { ignoreUnknownKeys = true }
                val searchResponse = try {
                    json.decodeFromString<OpenLibrarySearchResponse>(body)
                } catch (e: Exception) {
                    println("JSON parsing failed: ${e.message}")
                    return@withContext ""
                }

                // Same logic as before...
                val matchingDoc = searchResponse.docs.find {
                    it.coverId != null &&
                            it.title?.equals(title, ignoreCase = true) == true &&
                            it.authorName?.any { author1 ->
                                author1.equals(
                                    author,
                                    ignoreCase = true
                                )
                            } == true
                }
                val docToUse = matchingDoc ?: searchResponse.docs.firstOrNull { it.coverId != null }

                return@withContext docToUse?.coverId?.let { coverId ->
                    "https://covers.openlibrary.org/b/id/$coverId-L.jpg"
                }
            }
        }
    suspend fun findBookCoverUrls(client: OkHttpClient, title: String, author: String): List<String> =
        withContext(
            Dispatchers.IO
        )
        {
            var url = "https://openlibrary.org/search.json?title=${title.encodeUrl()}&author=${author.encodeUrl()}&limit=10"
            if (author.isBlank())
                url = "https://openlibrary.org/search.json?title=${title.encodeUrl()}&limit=10"
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("HTTP request failed with code ${response.code}")
                    return@withContext emptyList()
                }

                val body = response.body?.string() ?: return@withContext emptyList()

                val json = Json { ignoreUnknownKeys = true }
                val searchResponse = try {
                    json.decodeFromString<OpenLibrarySearchResponse>(body)
                } catch (e: Exception) {
                    println("JSON parsing failed: ${e.message}")
                    return@withContext emptyList()
                }

                val resutl = searchResponse.docs.filter { it.coverId != null }.map { it.coverId }
                    .map { "https://covers.openlibrary.org/b/id/$it-L.jpg" }

                return@withContext resutl
            }
        }

    @Serializable
    data class GoogleBooksResponse(
        val items: List<Item>? = null
    )
    {

        @Serializable
        data class Item(
            val volumeInfo: VolumeInfo? = null
        )

        @Serializable
        data class VolumeInfo(
            val title: String? = null,
            val authors: List<String>? = null,
            val imageLinks: ImageLinks? = null
        )

        @Serializable
        data class ImageLinks(
            @SerialName("smallThumbnail") val smallThumbnail: String? = null,
            val thumbnail: String? = null,
            val small: String? = null,
            val medium: String? = null,
            val large: String? = null,
            val extraLarge: String? = null
        )
    }

    suspend fun findGoogleBookCoverUrl(client: OkHttpClient, title: String, author: String): String? =
        withContext(
            Dispatchers.IO
        )
        {
            val encodedTitle = URLEncoder.encode(title, "UTF-8")
            val encodedAuthor = URLEncoder.encode(author, "UTF-8")
            val url = "https://www.googleapis.com/books/v1/volumes?q=intitle:$encodedTitle+inauthor:$encodedAuthor&maxResults=5"

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("Google Books API call failed: ${response.code}")
                    return@withContext ""
                }

                val body = response.body?.string() ?: return@withContext ""

                val json = Json { ignoreUnknownKeys = true }
                val booksResponse = json.decodeFromString<GoogleBooksResponse>(body)

                // Try to find a fit with imageLinks
                val item = booksResponse.items?.firstOrNull { it.volumeInfo?.imageLinks != null }

                // Return thumbnail or any available image URL
                var result = item?.volumeInfo?.imageLinks?.thumbnail
                    ?: item?.volumeInfo?.imageLinks?.smallThumbnail

                if (result == null)
                    result = ""
                return@withContext result
            }
        }

    fun findAmazonURL(title: String,author: String): String
    {
        val encodedTitle = URLEncoder.encode(title, "UTF-8")
        val encodedAuthor = URLEncoder.encode(author, "UTF-8")
        var url = "https://www.amazon.de/s?k=$encodedTitle,$encodedAuthor"
        if(author.isBlank())
            url = "https://www.amazon.de/s?k=$encodedTitle"

        return url
    }
}