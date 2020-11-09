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
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.NavController
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
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.product_info.*

class ProductInformationFragment : Fragment() {

    val args: ProductInformationFragmentArgs by navArgs()
    var stampList: ArrayList<StampItem>? = null
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Stamps/stamp")
    val mStorage = FirebaseStorage.getInstance()
    var stampBucket = "gs://mobile-assignment2.appspot.com"
    val mStoRef = mStorage.getReferenceFromUrl(stampBucket).child("images")
    lateinit var viewModel: StampViewModel
    var stampViewID = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        stampList = arrayListOf<StampItem>()
        viewModel = StampViewModel()
        stampViewID = args.productCodeArgument

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.product_info, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val currStamp = stampList?.toList()?.find {
//            it.stampID == stampViewID
//        }

//        val comments = CommentData().allComments().filter {
//            it.stampCode == stampCode
//        }
        /*
        * Comments block
        */
//        val comments = CommentData().allComments()
//
//        commentRecyclerView.apply {
//            layoutManager = LinearLayoutManager(activity)
//            adapter = CommentsAdapter(comments)
//        }
    }

    /**
     * Keep checking if new data or updated data comes out from the database and
     * revise it
     */
    override fun onResume() {
        super.onResume()

        viewModel.getStamps().observe(this, Observer { stamps ->
            if(stamps != null) {
                Log.d("----------||", stamps.toString())
                for (stamp in stamps) {
                    if (stamp.stampID == stampViewID) {
                        productTitle.text = stamp.name
                        productInfoDescription.text = stamp.description
                        context?.let { it1 -> Glide.with(it1).load(stamp.photo).into(stampPhotoMain) }

                        PushDownAnim.setPushDownAnimTo(checkUserProfileButton).setOnClickListener {
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
        })
    }

}