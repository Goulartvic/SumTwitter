package com.example.sumtwitter.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.example.sumtwitter.base.BaseViewModel
import com.example.sumtwitter.utils.RxHandler
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import io.reactivex.Maybe
import io.reactivex.MaybeOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : BaseViewModel() {

    private var firebaseAuth = FirebaseAuth.getInstance()

    var loginLiveData = MutableLiveData<Boolean>()

    fun isLoggedIn() {
        if (firebaseAuth.currentUser != null) {
            loginLiveData.postValue(true)
        }
    }

    fun startLogin(activity: Activity) {
        val provider = OAuthProvider.newBuilder("twitter.com")

        disposables.add(
            Maybe.create(MaybeOnSubscribe<AuthResult> {
                RxHandler.assignOnTask(
                    it,
                    firebaseAuth.startActivityForSignInWithProvider(activity, provider.build())
                )
            }).toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    loginLiveData.postValue(true)
                }, {
                    loginLiveData.postValue(false)
                })
        )
    }
}