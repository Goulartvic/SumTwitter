package com.example.sumtwitter.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sumtwitter.R
import com.example.sumtwitter.main.MainActivity
import com.example.sumtwitter.utils.Constants.Companion.SCREEN_NAME
import com.example.sumtwitter.utils.Constants.Companion.TOKEN
import com.example.sumtwitter.utils.showText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        observeViewModel()

        viewModel.isLoggedIn()

        login_button.setOnClickListener {
            viewModel.startLogin(this)
            loader.visibility = View.VISIBLE
            login_button.isEnabled = false
        }
    }

    private fun observeViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModel.nameLiveData.observe(this, Observer {
            it?.let {
                sharedPreferences.edit().putString(SCREEN_NAME, it).apply()
            }
            viewModel.getToken()
        })

        viewModel.loggedIn.observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

        viewModel.tokenLiveData.observe(this, Observer {
            it?.let {
            sharedPreferences
                .edit()
                .putString(TOKEN, it.tokenType.capitalize().plus(" ").plus(it.accessToken))
                .apply()
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

        viewModel.loginError.observe(this, Observer {
            loader.visibility = View.GONE
            login_button.isEnabled = true
            showText(this, getString(R.string.login_error))
        })
    }
}