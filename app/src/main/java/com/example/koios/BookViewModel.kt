package com.example.koios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModel (
    private val dao: BookDao
): ViewModel(){

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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(),emptyList())
    private val _state = MutableStateFlow(BookState())
    val state = combine(_state,_sortType,_books){state,sortType,books->
        state.copy(
            books = books,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BookState())

    fun onEvent(event: BookEvent){
        when(event){
            is BookEvent.DeleteBook -> {
                viewModelScope.launch {
                    dao.deleteBool(event.book)
                }

            }
            BookEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingBook = false
                ) }
            }
            BookEvent.SaveBook -> {
                val title = state.value.title
                val author = state.value.author
                val rating = state.value.rating
                val condition = state.value.condition
                val image = state.value.image
                val urlLink = state.value.urllink

                if (title.isBlank() || author.isBlank()){
                    return
                }

                val book = Book(
                    title = title,
                    author = author,
                    rating = rating,
                    condition = condition,
                    image = image,
                    urllink = urlLink
                )

                viewModelScope.launch {
                    dao.upsetBook(book)
                }

                _state.update { it.copy(
                    isAddingBook = false,
                    title = "",
                    author = "",
                    rating = -1,
                    condition = 0,
                    image = "",
                    urllink = ""
                ) }
            }
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
                    urllink = event.urllink
                ) }
            }
            is BookEvent.SetImage -> {
                _state.update { it.copy(
                    image = event.image
                ) }
            }
            BookEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingBook = true
                ) }
            }
            is BookEvent.SortBooks -> {
                _sortType.value = event.sortType
            }
        }
    }
}