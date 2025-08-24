package com.example.koios

data class BookState (
    val books: List<Book> = emptyList(),
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val image: String = "",
    val urlLink: String = "",
    val condition: Int = 0,
    val rating: Int = -1,
    val isAddingBook: Boolean = false,
    val sortType: SortType = SortType.CONDITION,
    val isLoading: Boolean = false,
    val searchText:String = "",
    val isChangeBook: Boolean = false,
    val isZooming: Boolean = false,
    val isImageChoose : Boolean = false,
    val imageOption : List<String> = emptyList<String>(),
    val currentimage: String = "",
    val imagePath: String = ""
)