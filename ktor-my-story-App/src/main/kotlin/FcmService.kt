package com.example

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

class FcmService {

    /**
     * Send a notification to a topic (e.g., "all")
     */
    fun sendToTopic(
        topic: String,
        title: String,
        body: String,
        data: Map<String, String>? = null
    ) {
        try {
            val messageBuilder = Message.builder()
                .setTopic(topic)
                .setNotification(com.google.firebase.messaging.Notification.builder().setTitle(title).setBody(body).build())

            data?.let { messageBuilder.putAllData(it) }

            val response = FirebaseMessaging.getInstance().send(messageBuilder.build())
            println("📬 FCM sent successfully: $response")
        } catch (e: Exception) {
            println("❌ FCM failed: ${e.message}")
        }
    }

    /**
     * Send a notification to a specific device using token
     */
    fun sendToToken(
        token: String,
        title: String,
        body: String,
        data: Map<String, String>? = null
    ) {
        try {
            val messageBuilder = Message.builder()
                .setToken(token)
                .setNotification(com.google.firebase.messaging.Notification.builder().setTitle(title).setBody(body).build())

            data?.let { messageBuilder.putAllData(it) }

            val response = FirebaseMessaging.getInstance().send(messageBuilder.build())
            println("📬 FCM sent to token successfully: $response")
        } catch (e: Exception) {
            println("❌ FCM to token failed: ${e.message}")
        }
    }
}

