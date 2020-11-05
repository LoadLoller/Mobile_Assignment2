package com.example.mobile_w01_07_5.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_w01_07_5.R
import com.example.mobile_w01_07_5.data.CommentData
import com.example.mobile_w01_07_5.data.StampData
import com.example.mobile_w01_07_5.data.StampItem
import com.example.mobile_w01_07_5.ui.Adapters.CommentsAdapter
import com.example.mobile_w01_07_5.ui.Adapters.StampsAdapter
import com.example.mobile_w01_07_5.ui.profile.OtherUserProfileFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.product_info.*

class ProductInformationFragment : Fragment() {

    val args: ProductInformationFragmentArgs by navArgs()
    var stampList: ArrayList<StampItem>? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        stampList = arrayListOf<StampItem>()
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("Stamps/stamp")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val stampsList = listOf(dataSnapshot.value)
                val stamps = stampsList.first() as List<*>

                for (stamp in stamps) {
//                    Log.d(">>>>>>>>>", stamp.toString())
                    val s = stamp as HashMap<*, *>
//                    for (stampItem in s.values) {
//                        val currentStamp = stampItem as HashMap<*, *>
                    val stampID = s.get("stampID").toString()
                    val userID = s.get("userID").toString()
                    val name = s.get("name").toString()
                    val rate = s.get("rate").toString().toInt()
                    val description = s.get("description").toString()
                    val locationX = s.get("locationX").toString().toDouble()
                    val locationY = s.get("locationY").toString().toDouble()
                    val photo = s.get("photo").toString()
                    val isHighlyRated = s.get("highlyRated").toString().toBoolean()
                    val stampItem = StampItem(stampID, userID, name, rate, description,
                            locationX, locationY, photo, isHighlyRated)
                    stampList?.add(stampItem)

                    val stampViewID = args.productCodeArgument

                    val stamp = stampList?.toList()?.find {
                        it.stampID == stampViewID
                    }

                    if (stamp != null) {
                        productTitle.text = stamp.name
                        productInfoDescription.text = stamp.description
                        stampPhotoMain.setImageDrawable(
                                ContextCompat.getDrawable(
                                        requireActivity(),
                                        Integer.parseInt(stamp.photo)
                                )
                        )

                        checkUserProfileButton.setOnClickListener {
                            val action =
                                    ProductInformationFragmentDirections.actionProductInfoToOtherUserProfile(stamp.userID)
                            it.findNavController().navigate(action)
                            requireActivity().getSharedPreferences("shopping_cart", Context.MODE_PRIVATE)
                                    .edit().apply {
                                        putString("latest_checked", "Last checked: " + stamp.name)
                                    }.apply()
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("--------------------++", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        myRef.addValueEventListener(postListener)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.product_info, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val comments = CommentData().allComments().filter {
//            it.stampCode == stampCode
//        }

        val comments = CommentData().allComments()

        commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CommentsAdapter(comments)
        }

//        productTitle.text = productCode
    }

}