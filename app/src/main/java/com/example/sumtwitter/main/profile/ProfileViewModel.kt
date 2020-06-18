package com.example.sumtwitter.main.profile

import androidx.lifecycle.MutableLiveData
import com.example.sumtwitter.BaseApplication
import com.example.sumtwitter.base.BaseViewModel
import com.example.sumtwitter.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileViewModel : BaseViewModel() {

    private var apiInstance = BaseApplication.apiInstance

    var user = MutableLiveData<User>()
    var error = MutableLiveData<Boolean>()

    fun getUser(auth: String, name: String) {
        apiInstance.getTweets(auth, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let {
                    user.postValue(it[0].user)
                }
            }, {
                error.postValue(true)
            })
    }

}