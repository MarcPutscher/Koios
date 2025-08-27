package com.example.koios.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title : String = "",
    val author: String = "",
    var image: String = "",
    var currentimage: String = "",
    var imagePath: String = "",
    val condition: Int = 0, // 0 = have to buy it , 1 = haven't read it , 2 = already read
    val rating: Int = -1, // from 0 to 10, where 0 is the worst and 10 is the best
    val urllink: String = "", // is a direct link where you can purchase this bool
    var titleMatch : Int = 0,
    var authorMatch : Int =  0
){
    fun doesMatchSearchQuery(query: String): Boolean{
        var matchingCombination = listOf(
            "$title$author",
            "$title $author",
        )

        if(!title.isBlank() and  !author.isBlank())
        {
            matchingCombination = listOf(
                "$title$author",
                "$title $author",
                "${title.first()} ${author.first()}"
            )
        }

        val result = matchingCombination.any{
            it.contains(query,ignoreCase = true)
        }

        if (result)
            markMatchingStat(query)
        return result
    }

    fun markMatchingStat(query: String)
    {
        titleMatch = 0
        if(title.contains(query,ignoreCase = true) or query.contains(title.first(), ignoreCase = true) )
            titleMatch = 1

        authorMatch = 0
        if(author.isEmpty())
            return

        if(author.contains(query,ignoreCase = true)or query.contains(author.first(), ignoreCase = true))
            authorMatch = 1

    }

    fun removeMarks(){
        titleMatch = 0
        authorMatch = 0
    }
}