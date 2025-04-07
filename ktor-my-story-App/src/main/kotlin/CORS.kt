package com.example
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import io.netty.handler.codec.http.cors.CorsConfigBuilder

fun Application.configureCORS() {
    install(CORS) {
        //CORS access the admin panel to make changes in backend it is use for communication
        // Allow requests from any origin also you can add ("url port")
        // for security to only specific url
        anyHost()
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
//        allowHeader(HttpHeaders.Authorization) // Allow auth headers if needed
//        allowCredentials = false // If you're using authentication like JWT
    }
}

