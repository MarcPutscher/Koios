package com.example.koios

sealed interface BookEvent {
    object SaveBook: BookEvent
    data class  SetTitle(val title: String): BookEvent
    data class  SetAuthor(val author: String): BookEvent
    data class  SetCondition(val condition: Int): BookEvent
    data class  SetRating(val rating: Int): BookEvent
    data class  SetURLLink(val urllink: String): BookEvent
    data class  SetImage(val image: String): BookEvent
    object ShowDialog: BookEvent
    object HideDialog: BookEvent
    data class SortBooks(val sortType: SortType): BookEvent
    data class DeleteBook(val book: Book): BookEvent
}