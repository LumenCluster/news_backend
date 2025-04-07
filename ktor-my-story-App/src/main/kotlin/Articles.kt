package com.example.ApplicationKt

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object Articles : Table() {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)
    val title = varchar("title", 255)
    val content = text("content")
    val name = varchar("name", 255) // category name
    // val category = integer("category")
    val category = reference("category",
        Categories, onDelete = ReferenceOption.CASCADE) // categoryID
    val imageUrl = text("image_url").nullable()
    val createdAt = long("created_at")
    val author = varchar("author", 255)
    val views = integer("views").default(0)

}


