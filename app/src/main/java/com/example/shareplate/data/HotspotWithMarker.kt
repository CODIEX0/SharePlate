package com.example.shareplate.data

import com.google.android.gms.maps.model.Marker

data class HotspotWithMarker(
    val hotspot: Hotspot,
    val marker: Marker?
)

