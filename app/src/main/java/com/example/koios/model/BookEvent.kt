package com.example.koios.model

import com.example.koios.types.DialogType
import com.example.koios.types.SortType

sealed interface BookEvent {
    object SaveBook: BookEvent
    data class  SetTitle(val title: String): BookEvent
    data class  SetAuthor(val author: String): BookEvent
    data class  SetCondition(val condition: Int): BookEvent
    data class  SetRating(val rating: Int): BookEvent
    data class  SetURLLink(val urlLink: String): BookEvent
    data class  SetImage(val image: String): BookEvent
    data class ShowDialog(val dialogType: DialogType): BookEvent
    object HideDialog: BookEvent
    object ZoomImage: BookEvent
    object EndZoomImage: BookEvent
    object GenerateImage: BookEvent
    object ToggleMenu: BookEvent
    object DeleteBooks: BookEvent
    object ImportBooks: BookEvent
    object ExportBooks: BookEvent
    object ImageBooks: BookEvent
    data class SortBooks(val sortType: SortType): BookEvent
    data class DeleteBook(val book: Book): BookEvent
    data class ChangeBook(val changeBook: Book): BookEvent
    data class  OnSearchTextChange(val searchText: String): BookEvent
    data class LoadURL(val url: String): BookEvent
    data class ImageSelected(val image: String): BookEvent
    data class InsertManyBooks(val input: String): BookEvent
    data class ShowMetadata(val currentBook: Book): BookEvent
    data class PDFBooks(val bookFilter: String): BookEvent
    data class SetBookListFilter(val bookFilter: String): BookEvent
}