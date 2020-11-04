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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_w01_07_5.R
import com.example.mobile_w01_07_5.data.CommentData
import com.example.mobile_w01_07_5.data.StampData
import com.example.mobile_w01_07_5.data.StampItem
import com.example.mobile_w01_07_5.ui.Adapters.CommentsAdapter
import com.example.mobile_w01_07_5.ui.Adapters.StampsAdapter
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

        val gson = Gson()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val ds = dataSnapshot
                val sType = object : TypeToken<List<StampItem>>() { }.type
                val stamps = gson.fromJson<List<StampItem>>(ds.getValue().toString(), sType)
//                Log.d("<<<<<<From HomeFragment", stamps.toString())

                for (stamp in stamps) {

                    stampList?.add(stamp)

                    val stampID = args.productCodeArgument

                    val stamp = stampList?.toList()?.find {
                        it.stampID == stampID
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
                            requireActivity().getSharedPreferences("shopping_cart", Context.MODE_PRIVATE).edit().apply {
                                putString("cart_amount", "twenty dollars")
                                putString("cart_tax", "twenty dollars")
                                putString("cart_quantity", "twenty dollars")
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


//        productTitile.text = productCode
    }

}