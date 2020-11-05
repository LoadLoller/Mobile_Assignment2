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
