package acharya.ajay.to_do_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import acharya.ajay.to_do_list.ui.TodoViewModel
import acharya.ajay.to_do_list.ui.components.AddTodoDialog
import acharya.ajay.to_do_list.ui.components.TodoItem
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TodoApp(viewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(viewModel: TodoViewModel) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showCompletedTasks by remember { mutableStateOf(false) }

    val todos by viewModel.todos.collectAsState()
    val activeTodos = todos.filter { !it.isCompleted }
    val completedTodos = todos.filter { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo List") },
                actions = {
                    IconButton(
                        onClick = { viewModel.deleteCompletedTodos() },
                        enabled = completedTodos.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete completed"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add todo")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (showCompletedTasks) "Completed Tasks" else "Active Tasks",
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = showCompletedTasks,
                    onCheckedChange = { showCompletedTasks = it }
                )
            }

            // Todo list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(if (showCompletedTasks) completedTodos else activeTodos) { todo ->
                    TodoItem(
                        todo = todo,
                        onToggleComplete = viewModel::toggleTodoCompletion,
                        onDelete = viewModel::deleteTodo
                    )
                }
            }
        }

        if (showAddDialog) {
            AddTodoDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, description, priority ->
                    viewModel.addTodo(title, description, priority)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {

   TodoApp(TodoViewModel(
       application = TODO(),
       todoDao = TODO()
   ))
   
}