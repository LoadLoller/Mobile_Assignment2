package com.example.stampViewer.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.stampViewer.R
import java.util.*
import java.time.LocalDateTime

class CommentData {
    @RequiresApi(Build.VERSION_CODES.O)
    fun allComments(): List<CommentItem> {
        return listOf<CommentItem>(
            CommentItem("TS", "sdfas", LocalDateTime.now(), "Yes I think so!"),
            CommentItem(
                "VBP",
                "sdfss",
                LocalDateTime.now(), "Yes I think so!"
            ),
            CommentItem("VNB", "UCB", LocalDateTime.now(), "Yes I think so!"),
            CommentItem("TS1", "Yosemite", LocalDateTime.now(), "Yes I think so!"),
            CommentItem("TS2", "Melbourne Square", LocalDateTime.now(), "Yes I think so!")
        )
    }
}