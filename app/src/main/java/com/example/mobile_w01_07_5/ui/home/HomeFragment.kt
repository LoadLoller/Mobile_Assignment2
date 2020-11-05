package com.example.mobile_w01_07_5.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_w01_07_5.R
import com.example.mobile_w01_07_5.data.StampData
import com.example.mobile_w01_07_5.data.StampItem
import com.example.mobile_w01_07_5.ui.Adapters.StampsAdapter
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var Stamps = StampData()
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

//                Log.d(">>>>>>>>>", stampsList.toString())
                for (stamp in stamps) {
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
//                    }
                }
                stampRecyclerView.adapter = StampsAdapter(stampList!!.toList())
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
