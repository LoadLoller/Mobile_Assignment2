package com.example.mobile_w01_07_5.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mobile_w01_07_5.R
import com.example.mobile_w01_07_5.data.StampItem
import com.example.mobile_w01_07_5.ui.Adapters.StampsAdapter
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    var stampList: ArrayList<StampItem>? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        stampList = arrayListOf<StampItem>()
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("Stamps/stamp")
        val mStorage = FirebaseStorage.getInstance()
        var stampBucket = "gs://mobile-assignment2.appspot.com"
        val mStoRef = mStorage.getReferenceFromUrl(stampBucket).child("images")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
//                val stampsList = listOf(dataSnapshot.value)
                stampList!!.clear()
                val stampsList = dataSnapshot.value as HashMap<*, *>
//                val stamps = stampsList.first() as List<*>
                for (stamps  in stampsList.values) {
                    var stamp = stamps as HashMap<*, *>
//                    val currentStamp = stampItem as HashMap<*, *>
                        val stampID = stamp.get("stampID").toString()
                        val userID = stamp.get("userID").toString()
                        val name = stamp.get("name").toString()
                        val rate = stamp.get("rate").toString().toInt()
                        val description = stamp.get("description").toString()
                        val locationX = stamp.get("locationX").toString().toDouble()
                        val locationY = stamp.get("locationY").toString().toDouble()
                        var photo = stamp.get("photo").toString()
                        var photoUrl = mStoRef.child(photo)
                        val localFile = File.createTempFile("images", "jpg")
                        photoUrl.downloadUrl.addOnSuccessListener {
                            val isHighlyRated = stamp.get("highlyRated").toString().toBoolean()
                            val stampItem = StampItem(stampID, userID, name, rate, description,
                                    locationX, locationY, it, isHighlyRated)
                            stampList?.add(stampItem)
//                            stampList!!.sortBy { it.name }
                            stampRecyclerView.adapter = StampsAdapter(stampList!!)
                        }.addOnFailureListener {
                            Log.d("EEEEEEEEEEEError from photo Url", photoUrl.toString())
                        }
                }
//                }
//                stampRecyclerView.adapter = StampsAdapter(stampList!!.toList())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("--------------------++", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        myRef.addValueEventListener(postListener)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

        var stampItems = stampList

        stampRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            if (stampItems != null) {
                adapter = StampsAdapter(stampItems.toList())
            }
        }
        stampRecyclerView.adapter?.notifyDataSetChanged()


        val textViewText = requireActivity().getSharedPreferences("shopping_cart", Context.MODE_PRIVATE)
                .getString("latest_checked", "Last checked: default")

        latestItemInCart.text = textViewText
    }
}
