package com.example.mobile_w01_07_5.data

import com.example.mobile_w01_07_5.R
import com.google.firebase.database.Exclude


data class StampItem(
    val stampID: String,
    val userID: String="",
    val name: String,
    val rate: Double = 0.0,
    val description: String,
    val locationX: Double,
    val locationY: Double,
    val photo: String = R.drawable.yosemite1.toString(),
    val isHighlyRated: Boolean = false
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "stampID" to stampID,
                "userID" to userID,
                "name" to name,
                "description" to description,
                "locationX" to locationX,
                "locationY" to locationY,
                "photo" to photo

        )
    }
}
