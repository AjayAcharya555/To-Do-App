package acharya.ajay.to_do_list.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import acharya.ajay.to_do_list.data.Todo
import acharya.ajay.to_do_list.data.TodoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.Typography.dagger

@HiltViewModel
class TodoViewModel @Inject constructor(
    application: Application,
    private val todoDao: TodoDao
) : AndroidViewModel(application) {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    private val _activeTodos = MutableStateFlow<List<Todo>>(emptyList())
    val activeTodos: StateFlow<List<Todo>> = _activeTodos

    private val _completedTodos = MutableStateFlow<List<Todo>>(emptyList())
    val completedTodos: StateFlow<List<Todo>> = _completedTodos

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            todoDao.getAllTodos()
                .catch { e -> e.printStackTrace() }
                .collect { todos ->
                    _todos.value = todos
                    _activeTodos.value = todos.filter { !it.isCompleted }
                    _completedTodos.value = todos.filter { it.isCompleted }
                }
        }
    }

    fun addTodo(title: String, description: String = "", priority: Todo.Priority = Todo.Priority.MEDIUM) {
        viewModelScope.launch {
            val todo = Todo(
                title = title,
                description = description,
                priority = priority
            )
            todoDao.insertTodo(todo)
        }
    }

    fun toggleTodoCompletion(todo: Todo) {
        viewModelScope.launch {
            todoDao.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.deleteTodo(todo)
        }
    }

    fun deleteCompletedTodos() {
        viewModelScope.launch {
            todoDao.deleteCompletedTodos()
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.updateTodo(todo)
        }
    }
} 