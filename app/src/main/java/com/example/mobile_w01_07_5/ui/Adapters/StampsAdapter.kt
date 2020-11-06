package com.example.mobile_w01_07_5.ui.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with
import com.example.mobile_w01_07_5.R
import com.example.mobile_w01_07_5.data.StampItem
import com.example.mobile_w01_07_5.ui.home.HomeFragment
import com.example.mobile_w01_07_5.ui.home.HomeFragmentDirections
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.a_single_stamp_row.view.*

class StampsAdapter(private val stampItem: List<StampItem>) :
    RecyclerView.Adapter<StampsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.a_single_stamp_row, parent, false)
//        this.notifyDataSetChanged()
        return ViewHolder(view)
    }

    /*How many items there gonna be*/
    override fun getItemCount() = stampItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stampItem = stampItem[position]
        holder.bind(stampItem)
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(stampItem: StampItem) {
            itemView.stampItemTitle.text = stampItem.name
//            itemView.foodPrice.text = foodItem.price.toString()
            itemView.stampRate.text = "Rate: ${stampItem.rate}"
            if (stampItem.isHighlyRated) {
                itemView.highlyRatedIcon.visibility = View.VISIBLE
            }
//            view.stampPhotoMain.setImageResource(Integer.parseInt(stampItem.photo))
//            Log.d("HHHHHHHH", stampItem.photo.toString())
            Glide.with(view.context).load(stampItem.photo).into(view.stampPhotoMain)

            /*  https://developer.android.com/guide/navigation/navigation-pass-data   */
            view.stampPhotoMain.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToProductInfo(stampItem.stampID)
                view.findNavController().navigate(action)
            }

        }
    }
}