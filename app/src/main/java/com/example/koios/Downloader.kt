package com.example.koios

interface Downloader {
    fun downloadFile(url :String,name:String,dir:String): Long
}