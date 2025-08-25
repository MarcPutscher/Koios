package com.example.koios

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Upsert
    suspend fun upsetBook(book: Book)

    @Delete
    suspend fun deleteBool(book: Book)

    @Query("SELECT * FROM book ORDER BY condition DESC")
    fun getBooksOrderedByCondition(): Flow<List<Book>>

    @Query("SELECT * FROM book ORDER BY title ASC")
    fun getBooksOrderedByTitle(): Flow<List<Book>>

    @Query("SELECT * FROM book ORDER BY author ASC")
    fun getBooksOrderedByAuthor(): Flow<List<Book>>

    @Query("SELECT * FROM book ORDER BY rating DESC")
    fun getBooksOrderedByRating(): Flow<List<Book>>

    @Query("SELECT * FROM book ORDER BY id DESC")
    fun getBooksOrderedById(): Flow<List<Book>>
}