package com.example.ApplicationKt
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        // val config = HikariConfig().apply {
        //     jdbcUrl = "jdbc:postgresql://localhost:5432/test"
        //     driverClassName = "org.postgresql.Driver"
        //     username = "postgres"
        //     password = "noor5"
        //     maximumPoolSize = 5
        // }
        //   val config = HikariConfig().apply {
        //     jdbcUrl = "jdbc:postgresql://ktor_news_app_db_user:HHe0fYdklak32jnFrN2Ce7s16BKCnRjD@dpg-d03u2649c44c738aupmg-a.frankfurt-postgres.render.com/ktor_news_app_db"
        //     driverClassName = "org.postgresql.Driver"
        //     username = "ktor_news_app_db_user"
        //     password = "HHe0fYdklak32jnFrN2Ce7s16BKCnRjD"
        //     maximumPoolSize = 8
        // }
        val config = HikariConfig().apply {
    jdbcUrl = "jdbc:postgresql://dpg-d03u2649c44c738aupmg-a.frankfurt-postgres.render.com:5432/ktor_news_app_db"
    driverClassName = "org.postgresql.Driver"
    username = System.getenv("DB_USER")
    password = System.getenv("DB_PASSWORD")
    maximumPoolSize = 8
}
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        transaction {
            SchemaInitializer.createTables()
        }
    }
}


