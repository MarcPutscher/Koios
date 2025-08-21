package com.example.koios

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
    fun save(){
        val title = state.value.title
        val author = state.value.author
        val rating = state.value.rating
        val condition = state.value.condition
        val image = state.value.image
        val urlLink = state.value.urlLink

        if (title.isBlank()){
            return
        }
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

        if(urlLink != "##insert##")
            viewModelScope.launch {
                dao.upsetBook(book)
            }
        else
            saveMany(book)

        setStateToDefault()
    }
    fun saveMany(defaultBook: Book){
        if(!defaultBook.title.isBlank()) {
            for(book in defaultBook.title.lines()){
                val a = book.split("#")
                var book2: Book
                if (a.size == 4){
                    book2 = Book(
                        title = a[0],
                        author = a[1],
                        rating = defaultBook.rating,
                        condition = a[3].toInt(),
                        image = defaultBook.image,
                        urllink = a[2]
                    )
                }
                else{
                    book2 = Book(
                        title = a[0],
                        author = "",
                        rating = defaultBook.rating,
                        condition = a[2].toInt(),
                        image = defaultBook.image,
                        urllink = a[1]
                    )
                }
                viewModelScope.launch {
                    dao.upsetBook(book2)
                }
            }

        }
    }
}