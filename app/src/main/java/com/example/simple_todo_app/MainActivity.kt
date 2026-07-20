package com.example.simple_todo_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.simple_todo_app.ui.theme.SimpleToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)

        val todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        setContent {
            val systemTheme = isSystemInDarkTheme()
            var isDarkTheme by remember {
                mutableStateOf(sharedPref.getBoolean("is_dark_theme", systemTheme))
            }
            var showTrashBin by remember { mutableStateOf(false) }

            SimpleToDoAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    if (showTrashBin) {
                        TrashBinPage(
                            viewModel = todoViewModel,
                            onBack = { showTrashBin = false }
                        )
                    } else {
                        TodoListPage(
                            viewModel = todoViewModel,
                            isDarkTheme = isDarkTheme,
                            onThemeToggle = {
                                isDarkTheme = !isDarkTheme
                                sharedPref.edit {
                                    putBoolean("is_dark_theme", isDarkTheme)
                                }
                            },
                            onOpenTrash = { showTrashBin = true }
                        )
                    }
                }
            }
        }
    }
}

