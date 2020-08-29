package me.emredirican.datastream.entity

import com.google.gson.annotations.SerializedName

data class Item(
    val browser: Browser,
    @SerializedName("geo")
    val geoLocation: GeoLocation,
    val rating: Long,
    val labels: Set<String>,
    val id: String
)

data class Browser(
    val version: String,
    val platform: String
)

data class GeoLocation(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double
)
