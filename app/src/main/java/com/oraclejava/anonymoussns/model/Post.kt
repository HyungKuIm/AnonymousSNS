package com.oraclejava.anonymoussns.model

data class Post(
    val postId: String,
    val writerId: String,
    val message: String,
    val writeTime: Any,
    val bgUri: String,
    val commentCount: Int = 0
)
