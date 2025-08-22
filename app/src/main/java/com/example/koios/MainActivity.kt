package com.example.koios

import android.Manifest
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.koios.ui.theme.KoiosTheme
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            BookDatabase::class.java,
            BookDatabase.NAME
        ).build()
    }

    private val viewModel by viewModels<BookViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BookViewModel(db.dao, activity = null) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(this,
            arrayOf(READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO),
            PackageManager.GET_PERMISSIONS
        )

        setContent {
            KoiosTheme {
                val state by viewModel.state.collectAsState()

                viewModel.activity = this

                BookScreen(state =state ,onEvent= viewModel::onEvent)
            }
        }
    }

    internal fun openUrl(link: String){
        val uri = link.toUri()
        val inte = Intent(Intent.ACTION_VIEW,uri)

        startActivity(inte)
    }
}