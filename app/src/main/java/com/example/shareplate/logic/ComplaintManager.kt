package com.example.shareplate.logic

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ComplaintManager {
    val complaints: DatabaseReference

    init {
        complaints = FirebaseDatabase.getInstance().getReference("complaints")
    }

    fun fileComplaint(userId: String, userType: String, complaintDetails: String) {
        val complaintId = complaints.push().key
        val complaintMap = HashMap<String, Any>()
        complaintMap["userId"] = userId
        complaintMap["userType"] = userType
        complaintMap["details"] = complaintDetails
        complaints.child(complaintId!!).setValue(complaintMap)
    }
}