package com.example.stampViewer

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stampViewer.data.CommentData
import com.example.stampViewer.data.StampData
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.product_info.*
import kotlinx.android.synthetic.main.product_info.stampPhotoMain

class ProductInformationFragment : Fragment() {

    val args: ProductInformationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.product_info, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stampCode = args.productCodeArgument

        val stamp = StampData().allStamps().find {
            it.stampCode == stampCode
        }

        if (stamp != null) {
            productTitle.text = stamp.name
            productInfoDescription.text = stamp.description
            stampPhotoMain.setImageDrawable(ContextCompat.getDrawable(requireActivity(), stamp.photo))


            checkUserProfileButton.setOnClickListener {
                requireActivity().getSharedPreferences("shopping_cart", Context.MODE_PRIVATE).edit().apply {
                    putString("cart_amount", "twenty dollars")
                    putString("cart_tax", "twenty dollars")
                    putString("cart_quantity", "twenty dollars")
                    putString("latest_checked", "Last checked: " + stamp.name)
                }.apply()
            }
        }

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