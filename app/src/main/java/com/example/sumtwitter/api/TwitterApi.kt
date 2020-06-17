package com.example.sumtwitter.api

import com.example.sumtwitter.model.Token
import com.example.sumtwitter.utils.Constants.Companion.GRANT_TYPE
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitterApi {

    @POST("oauth2/token")
    fun generateToken(
        @Header("Authorization") auth: String,
        @Query("grant_type") grantType: String = GRANT_TYPE
    ) : Observable<Token>

    @GET("/1.1/statuses/user_timeline.json")
    fun getTweets(
        @Header("Authorization") auth: String,
        @Query("user_id") id: Long,
        @Query("count") count: Long = 10
    ): Observable<Token>
}