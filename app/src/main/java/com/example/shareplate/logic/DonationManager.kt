package com.example.shareplate.logic

import com.example.shareplate.data.Hotspot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DonationManager {
    val donations: DatabaseReference

    init {
        donations = FirebaseDatabase.getInstance().getReference("donations")
    }

    fun addFoodDonation(
        donorId: String,
        cuisine: String,
        mealType: String,
        allergens: String,
        location: Hotspot,
        quantity: Int,
        pickupTime: String
    ) {
        val donationId = donations.push().key
        val donationMap = HashMap<String, Any>()
        donationMap["donorId"] = donorId
        donationMap["cuisine"] = cuisine
        donationMap["mealType"] = mealType
        donationMap["location"] = location
        donationMap["allergens"] = allergens
        donationMap["quantity"] = quantity
        donationMap["pickupTime"] = pickupTime
        donations.child(donationId!!).setValue(donationMap)
    }
}