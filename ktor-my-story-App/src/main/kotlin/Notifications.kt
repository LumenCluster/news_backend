package com.example.ApplicationKt
import org.jetbrains.exposed.dao.id.IntIdTable


object Notifications : IntIdTable() {

    val title = varchar("title", 255)
    val name = varchar("name", 255) // category name
    val imageUrl = text("image_url").nullable()
    val author = varchar("author", 255)
    val isRead = bool("is_read").default(false)
    val createdAt = long("created_at")
}
