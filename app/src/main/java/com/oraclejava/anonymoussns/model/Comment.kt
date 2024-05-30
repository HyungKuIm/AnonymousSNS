package com.oraclejava.anonymoussns.model

data class Comment (
    val commentId: String,
    val postId: String,
    val writerId: String,
    val message: String,
    val writeTime: Any,
    val bgUri: String
)
