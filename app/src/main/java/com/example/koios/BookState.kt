package com.example.koios

data class BookState (
    val books: List<Book> = emptyList(),
    val title: String = "",
    val author: String = "",
    val image: String = "",
    val urllink: String = "",
    val condition: Int = 0,
    val rating: Int = -1,
    val isAddingBook: Boolean = false,
    val sortType: SortType = SortType.CONDITION
)