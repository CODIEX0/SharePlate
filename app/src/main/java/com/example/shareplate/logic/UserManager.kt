package com.example.shareplate.logic

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserManager {
    private val database: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance().getReference("users")
    }

    fun addUser(userId: String?, userType: String?, email: String) {
        val userMap = HashMap<String, Any>()
        userMap["email"] = email
        userMap["status"] = "Initial" // Default status
        database.child(userType!!).child(userId!!).setValue(userMap)
    }

    fun updateUserStatus(userId: String?, userType: String?, status: String?) {
        database.child(userType!!).child(userId!!).child("status").setValue(status)
    }

    fun getUserRef(userType: String?): DatabaseReference {
        return database.child(userType!!)
    }
}