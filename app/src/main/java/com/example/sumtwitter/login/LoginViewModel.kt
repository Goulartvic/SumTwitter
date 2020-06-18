package com.example.sumtwitter.login

import android.app.Activity
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sumtwitter.BaseApplication
import com.example.sumtwitter.BuildConfig
import com.example.sumtwitter.base.BaseViewModel
import com.example.sumtwitter.model.Token
import com.example.sumtwitter.utils.Constants.Companion.BASIC_AUTH
import com.example.sumtwitter.utils.Constants.Companion.TWITTER_PROVIDER
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
    private var apiInstance = BaseApplication.apiInstance

    var tokenLiveData = MutableLiveData<Token>()
    var nameLiveData = MutableLiveData<String>()
    var loggedIn = MutableLiveData<Boolean>()
    var loginError = MutableLiveData<Boolean>()

    fun isLoggedIn() {
        if (firebaseAuth.currentUser != null) {
            loggedIn.postValue(true)
        }
    }

    fun startLogin(activity: Activity) {
        val provider = OAuthProvider.newBuilder(TWITTER_PROVIDER)

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
                    it.additionalUserInfo?.let {
                        nameLiveData.postValue(it.username)
                    }
                }, {
                    loginError.postValue(true)
                })
        )
    }

    fun getToken() {
        val encodedKey = Base64.encodeToString(BuildConfig.TWITTER_API_KEY.toByteArray(), Base64.DEFAULT)

        apiInstance.generateToken(
            BASIC_AUTH.plus(" ").plus(encodedKey.replace("\n", ""))
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                tokenLiveData.postValue(it)
            }, {
                it.message?.let {
                    loginError.postValue(true)
                }
            })
    }
}