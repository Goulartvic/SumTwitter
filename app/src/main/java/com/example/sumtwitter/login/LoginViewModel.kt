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

    var loginLiveData = MutableLiveData<Boolean>()
    var tokenLiveData = MutableLiveData<Token>()

    fun isLoggedIn() {
        if (firebaseAuth.currentUser != null) {
            loginLiveData.postValue(true)
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
                    loginLiveData.postValue(true)
                }, {
                    loginLiveData.postValue(false)
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
                    Log.e("TOKEN_ERROR", it)
                }
            })
    }
}