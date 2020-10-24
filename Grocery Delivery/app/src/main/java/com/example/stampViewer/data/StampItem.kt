package com.example.stampViewer.data

import com.example.stampViewer.R

data class StampItem(
    val stampCode: String,
    val name: String,
    val rate: Double,
    val description: String,
    val geo_location: String,
    val photo: Int = R.drawable.yosemite1,
    val isHighlyRated: Boolean = false
)
