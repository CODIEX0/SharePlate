package com.example.shareplate.logic

import com.example.shareplate.data.Request
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RequestManager {
    val requests: DatabaseReference

    init {
        requests = FirebaseDatabase.getInstance().getReference("requests")
    }
    data class Feedback(
        val rating: Double,
        val complaint: String
    )

    // Request food
    fun requestFood(doneeId: String, foodRequest: Request) {
        val db = FirebaseDatabase.getInstance()
        val doneeRef = db.getReference("donees").child(doneeId)
        val requestsRef = doneeRef.child("requests")
        requestsRef.push().setValue(foodRequest)
    }

    // Rate or file a complaint
    fun rateOrFileComplaint(doneeId: String, rating: Double, complaint: String) {
        val db = FirebaseDatabase.getInstance()
        val doneeRef = db.getReference("donees").child(doneeId)
        val feedbackRef = doneeRef.child("feedback")
        feedbackRef.push().setValue(Feedback(rating, complaint))
    }
}