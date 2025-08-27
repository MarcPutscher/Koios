package com.example.koios.model

import com.example.koios.types.DialogType
import com.example.koios.types.SortType

data class BookState (
    val books: List<Book> = emptyList(),
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val image: String = "",
    val urlLink: String = "",
    val condition: Int = 0,
    val rating: Int = -1,
    val currentImage: String = "",
    val imagePath: String = "",

    val sortType: SortType = SortType.CONDITION,
    val searchText:String = "",
    val imageOption : List<String> = emptyList<String>(),
    val dialogType: DialogType = DialogType.ADD,

    val showDialog: Boolean = false,
    val isAddingBook: Boolean = false,
    val isLoading: Boolean = false,
    val isChangeBook: Boolean = false,
    val isZooming: Boolean = false,
    val isImageChoose : Boolean = false,
    val isMenuOpen: Boolean = false,
)