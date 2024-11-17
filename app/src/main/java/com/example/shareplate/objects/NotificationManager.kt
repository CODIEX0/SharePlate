package com.example.shareplate.objects

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

object NotificationManager {

    fun sendNotificationToUser(userId: String, message: String) {
        // Retrieve the FCM token for the user from the database
        val database = FirebaseDatabase.getInstance()
        val userTokenRef = database.getReference("users").child(userId).child("fcmToken")

        userTokenRef.get().addOnSuccessListener { snapshot ->
            val userToken = snapshot.value as? String
            if (!userToken.isNullOrEmpty()) {
                // Send the notification using the FCM token
                sendPushNotification(userToken, message)
            } else {
                Log.e("NotificationManager", "Failed to retrieve FCM token for user $userId")
            }
        }.addOnFailureListener {
            Log.e("NotificationManager", "Error retrieving user token: ${it.message}")
        }
    }

    private fun sendPushNotification(token: String, message: String) {
        // Configure your FCM payload
        val payload = mapOf(
            "to" to token,
            "notification" to mapOf(
                "title" to "Food Request Alert",
                "body" to message
            ),
            "data" to mapOf(
                "type" to "foodRequest",
                "message" to message
            )
        )


    }
}
