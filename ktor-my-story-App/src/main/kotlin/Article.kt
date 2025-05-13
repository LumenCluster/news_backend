package com.example

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int? =null,
    val title: String,
    val content: String,
    val name: String, // category name
    val category: Int,
    val imageUrl: String?,
    val createdAt: Long,
    val author: String,
    val views : Int
)

@Serializable
data class Category(
    val id: Int,
    val name: String
)

