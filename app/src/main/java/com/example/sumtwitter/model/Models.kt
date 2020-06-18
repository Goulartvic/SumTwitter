package com.example.sumtwitter.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class Tweets (
    val result: List<Tweet>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tweet(
    @JsonProperty(value = "user") val user: User,
    @JsonProperty(value = "text") val text: String,
    @JsonProperty(value = "created_at") val createdAt: String,
    @JsonProperty(value = "retweet_count") val retweets: Long,
    @JsonProperty(value = "favorite_count") val favorites: Long
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    @JsonProperty(value = "name") val name: String,
    @JsonProperty(value = "screen_name") val screenName: String,
    @JsonProperty(value = "profile_image_url_https") val profileImage: String,
    @JsonProperty(value = "created_at") val createdAt: String,
    @JsonProperty(value = "description") val description: String
)

data class Token(
    @JsonProperty(value = "token_type") val tokenType: String,
    @JsonProperty(value = "access_token") val accessToken: String
)