package com.example.shareplate.logic

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BrokerManager {
    private val userManager: UserManager = UserManager()

    fun approveUser(userId: String?, userType: String?) {
        userManager.updateUserStatus(userId, userType, "Active")
    }

    fun suspendUser(userId: String?, userType: String?) {
        userManager.updateUserStatus(userId, userType, "Suspended")
    }

    fun resolveComplaint(complaintId: String?) {
        val complaintsRef = ComplaintManager().complaints
        complaintsRef.child(complaintId!!).removeValue()
    }

    // View profiles and requests
    fun viewProfilesAndRequests() {
        val db = FirebaseDatabase.getInstance()
        val donorsRef = db.getReference("donors")
        val doneesRef = db.getReference("donees")

        donorsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Handle donor data
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        doneesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Handle donee data
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Update donor or donee status
    fun updateStatus(userId: String, status: String) {
        val db = FirebaseDatabase.getInstance()
        val userRef = db.getReference("users").child(userId)
        userRef.child("status").setValue(status)
    }

    // Resolve a complaint
    fun resolveComplaint(complaintId: String, resolution: String) {
        val db = FirebaseDatabase.getInstance()
        val complaintsRef = db.getReference("complaints").child(complaintId)
        complaintsRef.child("resolution").setValue(resolution)
    }
}