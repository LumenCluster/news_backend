package com.example

import com.example.ApplicationKt.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080, host = "0.0.0.0",
                   module = Application::module).start(wait = true)

    // embeddedServer(Netty, port = 8080, host = "0.0.0.0",
    //     module = Application::module).start(wait = true)
}


fun Application.module() {

    // // Initialize Firebase only once
    // if (FirebaseApp.getApps().isEmpty()) {
    //     val serviceAccount = FileInputStream("src/main/resources/serviceAccountKey.json")

    //     val options = FirebaseOptions.builder()
    //         .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    //         .build()

    //     FirebaseApp.initializeApp(options)
    //     println("🔥 Firebase Initialized Successfully")
    // }

// if (FirebaseApp.getApps().isEmpty()) {
//     val serviceAccount = this::class.java.classLoader
//         .getResourceAsStream("serviceAccountKey.json")
//         ?: throw Exception("Firebase key not found in resources!")

//     val options = FirebaseOptions.builder()
//         .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//         .build()

//     FirebaseApp.initializeApp(options)
//     println("🔥 Firebase Initialized Successfully")
// }
// if (FirebaseApp.getApps().isEmpty()) {
//     val serviceAccountJson = System.getenv("FIREBASE_SERVICE_ACCOUNT")
//         ?: throw Exception("Firebase key not found in env variable!")

//     val options = FirebaseOptions.builder()
//         .setCredentials(GoogleCredentials.fromStream(serviceAccountJson.byteInputStream()))
//         .build()

//     FirebaseApp.initializeApp(options)
//     println("🔥 Firebase Initialized Successfully")
// }

    if (FirebaseApp.getApps().isEmpty()) {
        val serviceAccountJson = System.getenv("FIREBASE_SERVICE_ACCOUNT")
            ?: throw Exception("Firebase key not found in env variable!")

        println("➡ About to build FirebaseOptions...")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccountJson.byteInputStream()))
            .build()

        println("✅ FirebaseOptions built successfully!")

        FirebaseApp.initializeApp(options)
        println("🔥 Firebase Initialized Successfully")
    }
    
    DatabaseFactory.init()
    configureSerialization()
    configureCORS()
    configureRouting()
}
