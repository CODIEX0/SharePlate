package com.example.shareplate.data

data class Request(
    var requestId: String = "",
    var doneeUid: String = "",
    var donationId: String = "",
    var quantity: Int = 0,
    var pickupTime: String = ""
)
