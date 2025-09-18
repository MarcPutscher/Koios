package com.example.koios.service

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DownloadCompletedReceiver: BroadcastReceiver() {

    private lateinit var downloadManager: DownloadManager

    override fun onReceive(context: Context?, intent: Intent?) {
        downloadManager =context?.getSystemService(DownloadManager::class.java)!!
        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE"){
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            intent.getIntExtra(DownloadManager.COLUMN_STATUS, -1)
            if(id != -1L){
                println("Download with ID $id finished!")
            }

            // query download status
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(id))
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                if(index == -1)
                    return
                val status = cursor.getInt(index)
                if(status == DownloadManager.STATUS_FAILED){
                    downloadManager.remove(id)
                }
            }
        }
    }
}