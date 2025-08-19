package com.example.koios

import androidx.room.Database
import androidx.room.RoomDatabase
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