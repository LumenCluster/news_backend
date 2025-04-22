package com.example

import com.example.ApplicationKt.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080, host = "0.0.0.0",
                   module = Application::module).start(wait = true)

    // embeddedServer(Netty, port = 8080, host = "0.0.0.0",
    //     module = Application::module).start(wait = true)
}


fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureCORS()
    configureRouting()
}
