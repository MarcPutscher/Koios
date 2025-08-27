package com.example.koios.model

interface Downloader {
    fun downloadFile(url :String,name:String,dir:String): Long
}