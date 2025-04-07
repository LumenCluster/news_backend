package com.example.ApplicationKt
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/test"
            driverClassName = "org.postgresql.Driver"
            username = "postgres"
            password = "noor5"
            maximumPoolSize = 5
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        transaction {
            SchemaInitializer.createTables()
        }
    }
}


