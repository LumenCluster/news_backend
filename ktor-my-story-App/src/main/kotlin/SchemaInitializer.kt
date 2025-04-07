package com.example.ApplicationKt
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

    object SchemaInitializer {
        fun createTables() {
            transaction {
                SchemaUtils.create(Categories)
                SchemaUtils.create(Articles)

            }
        }
    }



