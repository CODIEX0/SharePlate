package com.example.shareplate.data

import com.example.shareplate.objects.Global.getCurrentDateTime

data class Hotspot(
    var id: String,
    var donation: String?,
    var dateTime: String,
    var lat: Double,
    var lng: Double,
){
    constructor() : this("", "", getCurrentDateTime(), 0.0, 0.0)
}