package com.example.shareplate.data

import com.example.shareplate.objects.Global.getCurrentDateTime

data class User (
    var uid: String? = null,
    var username: String? = "",
    var email: String? = "",
    var password: String? = "",
    var userImage: String? = "",
    var userType: String? = "", // "donor", "donee", "broker"
    var status: String? = "", // "Initial", "Active", "Inactive", "Suspended"
    var createdAt: String = getCurrentDateTime(), // Timestamp for when the user was created
    var updatedAt: String = getCurrentDateTime() // Timestamp for the last update
)