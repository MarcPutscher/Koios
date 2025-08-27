package com.example.koios

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.koios.ui.theme.KoiosTheme
import androidx.core.net.toUri
import com.example.koios.service.AndroidDownloader
import com.example.koios.service.BookDatabase
import com.example.koios.view.BookScreen
import com.example.koios.viewmodel.BookViewModel

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
                    return BookViewModel(db.dao, activity = null, downloader = null) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val downloader = AndroidDownloader(this)

        setContent {
            KoiosTheme {
                val state by viewModel.state.collectAsState()

                viewModel.activity = this
                viewModel.downloader = downloader

                BookScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
    }

    internal fun openUrl(link: String){
        val uri = link.toUri()
        val inte = Intent(Intent.ACTION_VIEW,uri)

        startActivity(inte)
    }
}