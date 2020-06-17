package com.example.sumtwitter.model

import com.squareup.moshi.Json
import java.util.*

data class Tweet(
    val name: String,
    val screenName: String,
    val text: String,
    val createdAt: Date,
    val profileImage: String
)

data class Token(
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "access_token") val accessToken: String
)