package com.example.mobile_w01_07_5.data

import android.util.Log
import com.google.firebase.database.*
import com.google.gson.Gson

class StampData {


    val stampList = arrayListOf<StampItem>()

    fun allStamps(): List<StampItem> {
        stampList.clear()
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("Stamps/stamp")
        val gson = Gson()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val stamps = listOf(dataSnapshot.getValue())

                for (stamp in stamps) {
                    val jsonString = gson.fromJson(stamp.toString(), StampItem::class.java)
                    val currentStamp = StampItem(jsonString.stampID, jsonString.userID, jsonString.name,
                            0, jsonString.description, jsonString.locationX,
                            jsonString.locationY)

                    stampList?.add(currentStamp)
                    Log.d("FFFFFFFFFFFFFFFFFFrom data FFF", stampList.toString())
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("--------------------++", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        myRef.addValueEventListener(postListener)



//        return stampList.plus(mockStamps)
        return stampList
    }

    fun update_to_database(mDatabase:FirebaseDatabase, mRef:DatabaseReference) {
        val gson = Gson()
//        val mockStamps = listOf<StampItem>(
//                StampItem("TS", "1", "Melbourne Square", 5, "Beautifulll", -17.123, 23.123),
//                StampItem(
//                        "VBP", "1",
//                        "MSU Museum",
//                        9,
//                        "Really Beautifulll. This is the musemum from Michigan State University. I really like it!",
//                        -17.123,
//                        23.123,
//                        photo = R.drawable.msu_museum.toString(),
//                        isHighlyRated = true
//                ),
//                StampItem("VNB", "1", "UCB", 9, "Beautifulll",
//                        -17.123, 23.123, photo = R.drawable.ucberkeley.toString()),
//                StampItem("TS1", "1", "Yosemite", 10, "Beautifulll",
//                        -17.123, 23.123, photo = R.drawable.yosemite.toString(), isHighlyRated = true)
////                StampItem("TS2", "sfa", "Melbourne Square", 5.0, "Beautifulll",
////                        -17.123, 23.123)
//        )
//        val jsonString = gson.toJson(mockStamps)
//        mRef.setValue(jsonString)
//        mRef.setValue(mockStamps)
    }
}