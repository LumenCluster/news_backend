package com.example

import io.ktor.http.*
//import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*  // Correct import
import io.ktor.serialization.kotlinx.json.*  // For JSON support
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureSerialization() {
        install(ContentNegotiation) {
            // Uses kotlinx.serialization
            json()
        }

//        gson {
//            setPrettyPrinting()
//            }

    routing {
        get("/json/kotlinx-serialization") {
                call.respond(mapOf("hello" to "world"))
            }
        get("/json/gson") {
                call.respond(mapOf("hello" to "world"))
            }
    }
}
