package com.example.shareplate.data

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class Donor(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val status: String, // Initial, Active, Inactive, Suspended
    val menu: List<FoodItem>
)

data class FoodItem(
    val id: String?,
    val donorId: String?,
    val name: String?,
    val cuisine: String?,
    val mealType: String?,
    val allergens: ArrayList<String>?,
    val quantity: Double,
    var picture: String? = "",
    val pickupTime: String? = "",
    val expiryDate: String? = "",
    val location: Hotspot,
    var collected: Boolean = false
)


