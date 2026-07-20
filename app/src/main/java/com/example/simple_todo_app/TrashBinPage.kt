package com.example.simple_todo_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simple_todo_app.utils.ListUtils
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashBinPage(
    viewModel: TodoViewModel,
    onBack: () -> Unit
) {
    val trashedTodos by viewModel.trashedTodos.observeAsState()
    var todoToDeletePermanently by remember { mutableStateOf<Todo?>(null) }

    if (todoToDeletePermanently != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { todoToDeletePermanently = null },
            title = { Text("Permanently Delete?") },
            text = { Text("This action cannot be undone. Are you sure you want to delete '${todoToDeletePermanently?.title}' forever?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePermanently(todoToDeletePermanently!!.id)
                        todoToDeletePermanently = null
                    }
                ) {
                    Text("DELETE", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { todoToDeletePermanently = null }) {
                    Text("CANCEL")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trash Bin") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Items in trash are automatically deleted after 7 days.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.error
            )

            trashedTodos?.let { list ->
                if (list.isEmpty()) {
                    EmptyTrashMessage()
                } else {
                    LazyColumn {
                        itemsIndexed(list) { index, item ->
                            val backgroundColor = if (index % 2 == 0) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                            TrashedItem(
                                item = item,
                                backgroundColor = backgroundColor,
                                onRestore = { viewModel.restoreTodo(item.id) },
                                onDeletePermanently = { todoToDeletePermanently = item }
                            )
                        }
                    }
                }
            } ?: EmptyTrashMessage()
        }
    }
}

@Composable
fun EmptyTrashMessage() {
    Text(
        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
        text = "Trash is empty",
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.outline
    )
}

@Composable
fun TrashedItem(
    item: Todo,
    backgroundColor: Color,
    onRestore: () -> Unit,
    onDeletePermanently: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Deleted: ${SimpleDateFormat("hh:mm a, dd/MM", Locale.ENGLISH).format(item.deletedAt!!)}",
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge
            )
            if (item.description.isNotBlank()) {
                Text(
                    text = ListUtils.formatAsList(item.description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
        IconButton(onClick = onRestore) {
            Icon(
                imageVector = Icons.Default.Restore,
                contentDescription = "Restore",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(onClick = onDeletePermanently) {
            Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = "Delete Permanently",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
