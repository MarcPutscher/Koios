package com.example.koios.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.koios.model.Book

@Database(
    entities = [Book::class],
    version = 1
)
abstract class BookDatabase: RoomDatabase() {
    companion object{
        const val NAME ="book_db"
    }
    abstract val dao: BookDao
}