package com.example.simple_todo_app

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simple_todo_app.utils.ListUtils
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListPage(
    viewModel: TodoViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onOpenTrash: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val todoList by viewModel.todoList.observeAsState()
    var inputText by remember { mutableStateOf("") }
    var inputDescription by remember { mutableStateOf(TextFieldValue("")) }

    var showEditDialog by remember { mutableStateOf(false) }
    var todoToEdit by remember { mutableStateOf<Todo?>(null) }
    var editedTitle by remember { mutableStateOf(TextFieldValue("")) }
    var editedDescription by remember { mutableStateOf(TextFieldValue("")) }

    if (showEditDialog && todoToEdit != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Update Task") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editedTitle,
                        onValueChange = { editedTitle = it },
                        label = { Text("Task Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedDescription,
                        onValueChange = { 
                            editedDescription = ListUtils.handleListLogic(editedDescription, it)
                        },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = ListUtils.ListVisualTransformation
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editedTitle.text.isNotBlank()) {
                            viewModel.updateTodo(todoToEdit!!.copy(title = editedTitle.text, description = editedDescription.text))
                            showEditDialog = false
                        }
                    },
                    enabled = editedTitle.text.isNotBlank() && (editedTitle.text != todoToEdit?.title || editedDescription.text != todoToEdit?.description)
                ) {
                    Text("UPDATE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("CANCEL")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = onOpenTrash) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Open Trash"
                        )
                    }
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = inputText,
                    onValueChange = { inputText = it },
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("Enter task...") },
                    singleLine = true
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = inputDescription,
                    onValueChange = { 
                        inputDescription = ListUtils.handleListLogic(inputDescription, it)
                    },
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("Enter description...") },
                    visualTransformation = ListUtils.ListVisualTransformation
                )
                Button(
                    modifier = Modifier.align(Alignment.End),
                    enabled = inputText.isNotBlank(),
                    onClick = {
                        val title = inputText
                        val description = inputDescription.text
                        
                        // 1. Clear focus first to dismiss keyboard and stop active editing
                        focusManager.clearFocus()
                        
                        // 2. Reset states
                        inputText = ""
                        inputDescription = TextFieldValue("")
                        
                        // 3. Add to database
                        viewModel.addTodo(title, description)
                    }) {
                    Text(text = "ADD")
                }
            }

            todoList?.let {
                LazyColumn(
                    content = {
                        itemsIndexed(it) { index: Int, item: Todo ->
                            val backgroundColor = if (index % 2 == 0) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                            val contentColor = if (index % 2 == 0) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }

                            TodoItem(
                                item = item,
                                backgroundColor = backgroundColor,
                                contentColor = contentColor,
                                onToggleComplete = { viewModel.toggleComplete(item) },
                                onDelete = { viewModel.moveToTrash(item.id) },
                                onEdit = {
                                    todoToEdit = item
                                    editedTitle = TextFieldValue(item.title)
                                    editedDescription = TextFieldValue(item.description)
                                    showEditDialog = true
                                }
                            )
                        }
                    }
                )
            } ?: Text(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                text = "No items yet",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun TodoItem(
    item: Todo,
    backgroundColor: Color,
    contentColor: Color,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val alpha = if (item.isCompleted) 0.5f else 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor.copy(alpha = alpha))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onToggleComplete) {
            Icon(
                imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Toggle Complete",
                tint = if (item.isCompleted) MaterialTheme.colorScheme.primary else contentColor.copy(alpha = 0.6f)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = SimpleDateFormat("hh:mm a, dd/MM", Locale.ENGLISH).format(item.createdAt),
                color = contentColor.copy(alpha = 0.5f * alpha),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = item.title,
                color = contentColor.copy(alpha = alpha),
                style = MaterialTheme.typography.titleLarge,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
            if (item.description.isNotBlank()) {
                Text(
                    text = ListUtils.formatAsList(item.description),
                    color = contentColor.copy(alpha = 0.6f * alpha),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
