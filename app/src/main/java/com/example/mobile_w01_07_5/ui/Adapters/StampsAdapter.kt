package com.example.mobile_w01_07_5.ui.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobile_w01_07_5.R
import com.example.mobile_w01_07_5.data.StampItem
import com.example.mobile_w01_07_5.ui.home.HomeFragmentDirections
import com.example.mobile_w01_07_5.ui.home.ProductInformationFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.a_single_stamp_row.view.*
import java.util.logging.Handler

class StampsAdapter(private var stampItem: List<StampItem>) :
        RecyclerView.Adapter<StampsAdapter.ViewHolder>() {
    var mDatabase = FirebaseDatabase.getInstance()
    var mRef = mDatabase.getReference("Stamps/stamp")
    var mAuth = FirebaseAuth.getInstance()

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

        /*
        * Like button handler
        */
        holder.itemView.likeButton.setOnClickListener {
            //                if (mAuth.currentUser?.uid !in stampItem.likedBy) {
            it.likeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_up_1_alt_30)
            holder.itemView.unLikeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_down_alt_30)
//            if (it.unLikeButton.cu != R.drawable.ic_baseline_thumb_down_alt_30)
//            it.unLikeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_down_alt_30)
//            val currStamp = stampItem.stampID.substring(0, stampItem.stampID.lastIndexOf("."))
            val currStamp = stampItem.stampID
            stampItem.rate += 1
            mRef.child(currStamp).child("rate").setValue(stampItem.rate)
            if (stampItem.rate >= 5) {
                mRef.child(currStamp).child("highlyRated").setValue(true)
                stampItem.isHighlyRated = true
            }
            
            // add current user to the stamp database
//                    stampItem.likedBy.add(mAuth.currentUser?.uid)
//                    myRef.child(currStamp).child("likedBy").setValue(stampItem.likedBy)
//                }
            notifyItemChanged(position)
            notifyDataSetChanged()
        }

        /*
        * Dislike button handler
        */
        holder.itemView.unLikeButton.setOnClickListener {
//                if (mAuth.currentUser?.uid in stampItem.likedBy) {
            it.unLikeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_down_1_alt_30)
            holder.itemView.likeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_up_alt_30)
//            it.likeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_up_alt_30)
//            val currStamp = stampItem.stampID.substring(0, stampItem.stampID.lastIndexOf("."))
            val currStamp = stampItem.stampID
            stampItem.rate -= 1
            mRef.child(currStamp).child("rate").setValue(stampItem.rate)
            if (stampItem.rate < 5) {
                mRef.child(currStamp).child("highlyRated").setValue(false)
                stampItem.isHighlyRated = false
            }

            // delete current user to the stamp database
//                    stampItem.likedBy.remove(mAuth.currentUser?.uid)
//                    myRef.child(currStamp).child("likedBy").setValue(stampItem.likedBy)
//                }
            notifyItemChanged(position)
            notifyDataSetChanged()
        }

        /*  https://developer.android.com/guide/navigation/navigation-pass-data   */
        holder.itemView.stampPhotoMain.setOnClickListener {
            val action =
                    HomeFragmentDirections.actionHomeFragmentToProductInfo(stampItem.stampID)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun submitList(stampItemList: List<StampItem>) {
        val oldList = stampItem
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
                StampItemDiffCallback(
                        oldList,
                        stampItemList
                )
        )
        stampItem = stampItemList
        diffResult.dispatchUpdatesTo(this)
    }

    class StampItemDiffCallback(
            var oldStampItemList: List<StampItem>,
            var newStampItemList: List<StampItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldStampItemList.size
        }

        override fun getNewListSize(): Int {
            return newStampItemList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldStampItemList.get(oldItemPosition).stampID == newStampItemList.get(newItemPosition).stampID)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldStampItemList.get(oldItemPosition).equals(newStampItemList.get(newItemPosition))
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(stampItem: StampItem) {
            itemView.stampItemTitle.text = stampItem.name
            itemView.stampRate.text = "Rate: ${stampItem.rate}"
            if (stampItem.rate > 5 || stampItem.isHighlyRated)
                itemView.highlyRatedIcon.visibility = View.VISIBLE
//            view.stampPhotoMain.setImageResource(Integer.parseInt(stampItem.photo))
            Glide.with(view.context).load(stampItem.photo).into(view.stampPhotoMain)
        }
    }
}