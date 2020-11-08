package com.example.mobile_w01_07_5.data

import android.net.Uri
import com.example.mobile_w01_07_5.R
import com.google.firebase.database.Exclude
import com.google.firebase.storage.StorageReference
import java.net.URL


data class StampItem(
        val stampID: String,
        val userID: String="",
        val name: String,
        val rate: Int = 0,
        val description: String,
        val locationX: Double,
        val locationY: Double,
//        val photo: String = R.drawable.yosemite1.toString(),
        val photo: Uri? = null,
        val isHighlyRated: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as StampItem

        if (stampID != other.stampID) {
            return false
        }
        if (userID != other.userID) {
            return false
        }
        if (name != other.name) {
            return false
        }
        if (rate != other.rate) {
            return false
        }
        if (description != other.description) {
            return false
        }
        if (locationX != other.locationX) {
            return false
        }
        if (locationY != other.locationY) {
            return false
        }
//        if (likedBy != other.likedBy) {
//            return false
//        }

        return true
    }

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
