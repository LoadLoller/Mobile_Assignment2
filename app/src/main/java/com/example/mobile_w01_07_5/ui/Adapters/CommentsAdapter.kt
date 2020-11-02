package com.example.mobile_w01_07_5.ui.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_w01_07_5.R
import com.example.mobile_w01_07_5.data.CommentItem
import kotlinx.android.synthetic.main.row_comment.view.*

class CommentsAdapter (private val commentItem: List<CommentItem>):
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_comment, parent, false)
            return ViewHolder(view)
        }

        /*How many items there gonna be*/
        override fun getItemCount() = commentItem.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val commentItem = commentItem[position]
            holder.bind(commentItem)
        }

        class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            fun bind(commentItem: CommentItem) {
                itemView.comment_username.text = commentItem.userID
//            itemView.foodPrice.text = foodItem.price.toString()
                itemView.comment_content.text = commentItem.comment
                itemView.comment_date.text = commentItem.date.toString()

                view.comment_user_img.setImageResource(R.drawable.fresh_salad)

//                /*  https://developer.android.com/guide/navigation/navigation-pass-data   */
//                view.stampPhotoMain.setOnClickListener {
//                    val action =
//                        FirstFragmentDirections.actionFirstFragmentToProductInfo(commentItem.stampCode)
//                    view.findNavController().navigate(action)
//                }

            }
        }

}