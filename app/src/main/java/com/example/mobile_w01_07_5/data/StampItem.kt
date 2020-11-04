package com.example.mobile_w01_07_5.data

import com.example.mobile_w01_07_5.R


data class StampItem(
    val stampCode: String,
    val name: String,
    val rate: Double,
    val description: String,
    val geo_location: String,
    val photo: Int = R.drawable.yosemite1,
    val isHighlyRated: Boolean = false
)
