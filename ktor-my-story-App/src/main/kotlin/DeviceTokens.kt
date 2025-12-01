package com.example.ApplicationKt
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table



object DeviceTokens : Table("device_token") {
    val id = integer("id").autoIncrement()
    val userId = varchar("user_id", 50)
    val token = text("token")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}
