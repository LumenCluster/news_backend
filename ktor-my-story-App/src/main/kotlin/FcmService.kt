package com.example

// import com.google.firebase.messaging.AndroidConfig
// import com.google.firebase.messaging.AndroidNotification
// import com.google.firebase.messaging.FirebaseMessaging
// import com.google.firebase.messaging.Message
// import com.google.firebase.messaging.Notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.Notification

class FcmService {
    fun sendToTopic(
        topic: String,
        title: String,
        body: String,
        data: Map<String, String>? = null
    ) {
        try {
            val finalData = data?.toMutableMap() ?: mutableMapOf()
            // Do NOT add click_action here

            val message = Message.builder()
                .setTopic(topic)
                // This top-level notification ensures system shows it when app is killed
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .setAndroidConfig(
                    AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(
                            AndroidNotification.builder()
                                .setChannelId("my_channel") // MUST MATCH Flutter
                                .setClickAction("FLUTTER_NOTIFICATION_CLICK") // For Flutter tap handling
                                .build()
                        )
                        .build()
                )
                .putAllData(finalData) // only custom keys, e.g., articleId
                .build()
            println("📦 Data Payload: $finalData")


            val response = FirebaseMessaging.getInstance().send(message)
            println("📬 FCM sent successfully: $response")
        } catch (e: Exception) {
            println("❌ FCM failed: ${e.message}")
        }
    }


    fun sendToToken(
        token: String,
        title: String,
        body: String,
        data: Map<String, String>? = null
    ) {
        try {
            val finalData = data?.toMutableMap() ?: mutableMapOf()
            finalData["title"] = title
            finalData["body"] = body
            finalData["articleId"] = finalData["articleId"] ?: "0"

            val message = Message.builder()
                .setToken(token)
                .setAndroidConfig(
                    AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build()
                )
                .putAllData(finalData)
                .build()

            println("📦 Data Payload for token $token: $finalData")
            val response = FirebaseMessaging.getInstance().send(message)
            println("📬 FCM sent to token: $response")
        } catch (e: Exception) {
            println("❌ FCM to token failed: ${e.message}")
        }
    }
}







// class FcmService {
//     fun sendToTopic(
//         topic: String,
//         title: String,
//         body: String,
//         data: Map<String, String>? = null
//     ) {
//         try {
//             val finalData = data?.toMutableMap() ?: mutableMapOf()
//             // Do NOT add click_action here

//             val message = Message.builder()
//                 .setTopic(topic)
//                 // This top-level notification ensures system shows it when app is killed
//                 .setNotification(
//                     Notification.builder()
//                         .setTitle(title)
//                         .setBody(body)
//                         .build()
//                 )
//                 .setAndroidConfig(
//                     AndroidConfig.builder()
//                         .setPriority(AndroidConfig.Priority.HIGH)
//                         .setNotification(
//                             AndroidNotification.builder()
//                                 .setChannelId("my_channel") // MUST MATCH Flutter
//                                 .setClickAction("FLUTTER_NOTIFICATION_CLICK") // For Flutter tap handling
//                                 .build()
//                         )
//                         .build()
//                 )
//                 .putAllData(finalData) // only custom keys, e.g., articleId
//                 .build()
//             println("📦 Data Payload: $finalData")


//             val response = FirebaseMessaging.getInstance().send(message)
//             println("📬 FCM sent successfully: $response")
//         } catch (e: Exception) {
//             println("❌ FCM failed: ${e.message}")
//         }
//     }


//     fun sendToToken(
//         token: String,
//         title: String,
//         body: String,
//         data: Map<String, String>? = null
//     ) {
//         try {
//             val finalData = data?.toMutableMap() ?: mutableMapOf()
//             // Include only custom keys like articleId
//             finalData["articleId"] = finalData["articleId"] ?: "0"

//             val message = Message.builder()
//                 .setToken(token)
//                 .setNotification(
//                     Notification.builder()
//                         .setTitle(title)
//                         .setBody(body)
//                         .build()
//                 )
//                 .setAndroidConfig(
//                     AndroidConfig.builder()
//                         .setPriority(AndroidConfig.Priority.HIGH)
//                         .setNotification(
//                             AndroidNotification.builder()
//                                 .setChannelId("my_channel")
//                                 .setClickAction("FLUTTER_NOTIFICATION_CLICK")
//                                 .build()
//                         )
//                         .build()
//                 )
//                 .putAllData(finalData)
//                 .build()
//             println("📦 Data Payload: $finalData")

//             val response = FirebaseMessaging.getInstance().send(message)
//             println("📬 FCM sent to token: $response")
//         } catch (e: Exception) {
//             println("❌ FCM to token failed: ${e.message}")
//         }
//     }





// }
