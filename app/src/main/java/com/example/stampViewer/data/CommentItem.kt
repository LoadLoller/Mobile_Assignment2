package com.example.stampViewer.data

import java.time.LocalDateTime

class CommentItem (
    val stampCode: String,
    val userID: String,
    val date: LocalDateTime,
    val comment: String
)