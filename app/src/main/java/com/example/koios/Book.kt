package com.example.koios

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "book_table")
data class Book (
    @PrimaryKey(autoGenerate = true)
    val id: UUID = UUID.randomUUID(),
    val title : String = "",
    val author: String = "",
    val image: String = "",
    val condition: Int = 0, // 0 = have to buy it , 1 = haven't read it , 2 = already read
    val rating: Int = -1, // from 0 to 10, where 0 is the worst and 10 is the best
    val urllink: String = "" // is a direct link where you can purchase this bool
)