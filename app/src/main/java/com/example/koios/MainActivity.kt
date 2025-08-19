package com.example.koios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.koios.ui.theme.KoiosTheme

class MainActivity : ComponentActivity() {

    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            BookDatabase::class.java,
            "book.db"
        ).build()
    }

    private val viewModel by viewModels<BookViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BookViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KoiosTheme {
                val state by viewModel.state.collectAsState()

                BookScreen(state =state ,onEvent= viewModel::onEvent)
            }
        }
    }
}