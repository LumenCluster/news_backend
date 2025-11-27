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
            val message = Message.builder()
                .setTopic(topic)
                .setAndroidConfig(
                    AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(
                            AndroidNotification.builder()
                                .setTitle(title) // REQUIRED for killed state
                                .setBody(body)
                                .setChannelId("my_channel") // MUST MATCH FLUTTER
                                .setClickAction("FLUTTER_NOTIFICATION_CLICK") // REQUIRED
                                .setSound("default")
                                .build()
                        )
                        .build()
                )
                .putAllData(data ?: emptyMap())
                .build()

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
            val message = Message.builder()
                .setToken(token)
                .setAndroidConfig(
                    AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(
                            AndroidNotification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setChannelId("my_channel")
                                .setClickAction("FLUTTER_NOTIFICATION_CLICK")
                                .setSound("default")
                                .build()
                        )
                        .build()
                )
                .putAllData(data ?: emptyMap())
                .build()

            val response = FirebaseMessaging.getInstance().send(message)
            println("📬 FCM sent to token: $response")
        } catch (e: Exception) {
            println("❌ FCM to token failed: ${e.message}")
        }
    }
}


// class FcmService {

//     /**
//      * Send a notification to a topic (e.g., "all")
//      */
//     fun sendToTopic(
//         topic: String,
//         title: String,
//         body: String,
//         data: Map<String, String>? = null
//     ) {
//         try {
//             val messageBuilder = Message.builder()
//                 .setTopic(topic)
//                 .setNotification(com.google.firebase.messaging.Notification.builder().setTitle(title).setBody(body).build())

//             data?.let { messageBuilder.putAllData(it) }

//             val response = FirebaseMessaging.getInstance().send(messageBuilder.build())
//             println("📬 FCM sent successfully: $response")
//         } catch (e: Exception) {
//             println("❌ FCM failed: ${e.message}")
//         }
//     }

//     /**
//      * Send a notification to a specific device using token
//      */
//     fun sendToToken(
//         token: String,
//         title: String,
//         body: String,
//         data: Map<String, String>? = null
//     ) {
//         try {
//             val messageBuilder = Message.builder()
//                 .setToken(token)
//                 .setNotification(com.google.firebase.messaging.Notification.builder().setTitle(title).setBody(body).build())

//             data?.let { messageBuilder.putAllData(it) }

//             val response = FirebaseMessaging.getInstance().send(messageBuilder.build())
//             println("📬 FCM sent to token successfully: $response")
//         } catch (e: Exception) {
//             println("❌ FCM to token failed: ${e.message}")
//         }
//     }
// }

