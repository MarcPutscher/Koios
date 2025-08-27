package com.example.koios.service

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.example.koios.model.Downloader
import java.io.File

class AndroidDownloader(
    private val context: Context
): Downloader {
    private val downloadManger = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String, name: String, dir:String): Long{
        //set file directory
        val filedir:String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path +"/Koios/Images"
        if(!File(filedir).exists())
            File(filedir).mkdir()

        val dirpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path +"/"+dir
        if(!File(dirpath).exists())
            File(dirpath).mkdir()

        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpg")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("$name.jpg")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,dir+"/image.jpg")
        return downloadManger.enqueue(request)
    }
}