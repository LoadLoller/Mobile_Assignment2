package com.example.mobile_w01_07_5.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mobile_w01_07_5.R
import com.example.mobile_w01_07_5.data.CommentData
import com.example.mobile_w01_07_5.data.StampItem
import com.example.mobile_w01_07_5.ui.Adapters.CommentsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.a_single_stamp_row.view.*
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
        val mStorage = FirebaseStorage.getInstance()
        var stampBucket = "gs://mobile-assignment2.appspot.com"
        val mStoRef = mStorage.getReferenceFromUrl(stampBucket).child("images")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
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
                    photoUrl.downloadUrl.addOnSuccessListener {
                        val isHighlyRated = stamp.get("highlyRated").toString().toBoolean()
                        val stampItem = StampItem(stampID, userID, name, rate, description,
                                locationX, locationY, it, isHighlyRated)
                        stampList?.add(stampItem)

                        val stampViewID = args.productCodeArgument

                        val currStamp = stampList?.toList()?.find {
                            it.stampID == stampViewID
                        }

                        if (currStamp != null) {
                            productTitle.text = currStamp.name
                            productInfoDescription.text = currStamp.description
                            context?.let { it1 -> Glide.with(it1).load(currStamp.photo).into(stampPhotoMain) }
//                            Picasso.get().load(currStamp.photo).into(stampPhotoMain)
//                        stampPhotoMain.setImageDrawable(
//                                ContextCompat.getDrawable(
//                                        requireActivity(),
//                                        Integer.parseInt(stamp.photo)
//                                )
//                        )

                            checkUserProfileButton.setOnClickListener {
                                val action =
                                        ProductInformationFragmentDirections.actionProductInfoToOtherUserProfile(currStamp.userID)
                                it.findNavController().navigate(action)
                                requireActivity().getSharedPreferences("shopping_cart", Context.MODE_PRIVATE)
                                        .edit().apply {
                                            putString("latest_checked", "Last checked: " + currStamp.name)
                                        }.apply()
                            }
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