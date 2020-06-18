package com.example.sumtwitter.main.timeline

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sumtwitter.BaseApplication
import com.example.sumtwitter.base.BaseViewModel
import com.example.sumtwitter.model.Tweet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TimelineViewModel : BaseViewModel() {

    private var apiInstance = BaseApplication.apiInstance

    var tweetList = MutableLiveData<List<Tweet>>()
    var error = MutableLiveData<Boolean>()


    fun getTweets(auth: String, name: String) {
        apiInstance.getTweets(auth, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let {
                    tweetList.postValue(it)
                }
            }, {
                error.postValue(true)
            })
    }

}