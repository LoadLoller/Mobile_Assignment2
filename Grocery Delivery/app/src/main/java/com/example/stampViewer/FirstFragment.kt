package com.example.stampViewer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stampViewer.data.StampData
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }


        val stampItems = StampData().allStamps()
//        val foodItems = listOf<FoodItem>(
//            FoodItem("TS","Tofu Scramble", 10.99, "Delicious"),
//            FoodItem("VBP","Vegan Breakfast Platter", 4.2, "Delicious", true),
//            FoodItem("VNB","Vegan Nom Burrito", 8.99, "Delicious"),
//            FoodItem("TS1","Tofu Scramble", 1.32, "Delicious", true),
//            FoodItem("TS2","Tofu Scramble", 23.23, "Delicious")
//        )

        stampRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = StampsAdapter(stampItems)
        }


        val textViewText = requireActivity().getSharedPreferences("shopping_cart", Context.MODE_PRIVATE)
            .getString("latest_checked", "Last checked: default")
        
        latestItemInCart.text = textViewText
    }
}