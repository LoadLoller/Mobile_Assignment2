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
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var Stamps = StampData()
    var stampList : ArrayList<StampItem>? = null

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
                    stampRecyclerView.adapter = StampsAdapter(stampList!!.toList())
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

//        Stamps.initialize()
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
