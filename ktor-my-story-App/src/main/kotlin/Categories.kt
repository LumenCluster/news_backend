package com.example.ApplicationKt

import org.jetbrains.exposed.dao.id.IntIdTable


//IntIDTable() auto increment the category id
object Categories : IntIdTable() {
    val name = varchar("name", 255) // category name
}
