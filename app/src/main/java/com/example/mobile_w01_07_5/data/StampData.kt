package com.example.mobile_w01_07_5.data

import com.example.mobile_w01_07_5.R


class StampData {

    fun allStamps(): List<StampItem> {
        return listOf<StampItem>(
            StampItem("TS", "Melbourne Square", 5.0, "Beautifulll", "-17,123, 23,123", likeCount = 0),
            StampItem(
                "VBP",
                "MSU Museum",
                9.0,
                "Really Beautifulll. This is the musemum from Michigan State University. I really like it!",
                "-17,123, 23,123",
                photo = R.drawable.msu_museum,
                isHighlyRated = true,
                    likeCount = 0
            ),
            StampItem("VNB", "UCB", 9.0, "Beautifulll",
                "-17,123, 23,123", photo = R.drawable.ucberkeley, likeCount = 0),
            StampItem("TS1", "Yosemite", 10.0, "Beautifulll",
                "-17,123, 23,123", photo = R.drawable.yosemite, isHighlyRated = true, likeCount = 0),
            StampItem("TS2", "Melbourne Square", 5.0, "Beautifulll",
                "-17,123, 23,123", likeCount = 0)
        )
    }
}