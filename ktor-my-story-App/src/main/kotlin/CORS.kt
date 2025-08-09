package com.example
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS() {
    install(CORS) {
        //CORS access the admin panel to make changes in backend it is use for communication
        // Allow requests from any origin also you can add ("url port")
        // for security to only specific url
        // allowHost("news-backend-88pj.onrender.com", schemes = listOf("https"))
        anyHost()
        // Only allow the admin dashboard domain
        allowHost("news-frontend-xoxz.onrender.com", schemes = listOf("https"))
        
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
    }
}






