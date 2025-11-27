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

                // ⭐ CRITICAL FIX: Add top-level Notification for display reliability (iOS & Android killed state)
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
                                // The title/body are included in the top-level Notification now
                                .setChannelId("my_channel") // MUST MATCH FLUTTER
                                .setClickAction("FLUTTER_NOTIFICATION_CLICK") // REQUIRED
                                .setSound("default")
                                .build()
                        )
                        .build()
                )
                .putAllData(data ?: emptyMap()) // Contains your deep link data (e.g., articleId)
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
            // Prepare data payload
            val finalData = data?.toMutableMap() ?: mutableMapOf()
            finalData["title"] = title
            finalData["body"] = body
            finalData["click_action"] = "FLUTTER_NOTIFICATION_CLICK"
            finalData["channel_id"] = "my_channel"

            val message = Message.builder()
                .setToken(token)

                // Top-level Notification (needed for iOS and killed-state Android)
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )

                // Explicit Android Config (needed for background/killed Android)
                .setAndroidConfig(
                    AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(
                            AndroidNotification.builder()
                                .setChannelId("my_channel")
                                .setClickAction("FLUTTER_NOTIFICATION_CLICK")
                                .setSound("default")
                                .build()
                        )
                        .build()
                )

                // Attach final data payload for deep linking
                .putAllData(finalData)
                .build()

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
//             val message = Message.builder()
//                 .setTopic(topic)
//                 .setAndroidConfig(
//                     AndroidConfig.builder()
//                         .setPriority(AndroidConfig.Priority.HIGH)
//                         .setNotification(
//                             AndroidNotification.builder()
//                                 .setTitle(title) // REQUIRED for killed state
//                                 .setBody(body)
//                                 .setChannelId("my_channel") // MUST MATCH FLUTTER
//                                 .setClickAction("FLUTTER_NOTIFICATION_CLICK") // REQUIRED
//                                 .setSound("default")
//                                 .build()
//                         )
//                         .build()
//                 )
//                 .putAllData(data ?: emptyMap())
//                 .build()

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
//             val message = Message.builder()
//                 .setToken(token)
//                 .setAndroidConfig(
//                     AndroidConfig.builder()
//                         .setPriority(AndroidConfig.Priority.HIGH)
//                         .setNotification(
//                             AndroidNotification.builder()
//                                 .setTitle(title)
//                                 .setBody(body)
//                                 .setChannelId("my_channel")
//                                 .setClickAction("FLUTTER_NOTIFICATION_CLICK")
//                                 .setSound("default")
//                                 .build()
//                         )
//                         .build()
//                 )
//                 .putAllData(data ?: emptyMap())
//                 .build()

//             val response = FirebaseMessaging.getInstance().send(message)
//             println("📬 FCM sent to token: $response")
//         } catch (e: Exception) {
//             println("❌ FCM to token failed: ${e.message}")
//         }
//     }
// }


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

