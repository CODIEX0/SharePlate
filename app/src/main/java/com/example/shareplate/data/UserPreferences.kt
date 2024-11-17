package com.example.shareplate.data

data class UserPreferences(
    var userUID: String? = null,
    var unitSystem: String = "Metric",
    var maxRadius: Int = 20
)