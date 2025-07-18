package acharya.ajay.to_do_list.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null
) {
    enum class Priority {
       LOW, HIGH, MEDIUM

    }
}

//enum class Priority {
//    LOW, MEDIUM, HIGH
//}