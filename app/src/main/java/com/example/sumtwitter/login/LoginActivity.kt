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
            login_loader.visibility = View.VISIBLE
            login_button.isEnabled = false
        }
    }

    private fun observeViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModel.loginLiveData.observe(this, Observer {
            if (it) {
               viewModel.getToken()
                return@Observer
            }

            login_loader.visibility = View.GONE
            login_button.isEnabled = true
            showText(this, "Não foi possível realizar o login")
        })

        viewModel.tokenLiveData.observe(this, Observer {
            it?.let {
            sharedPreferences.edit().putString(
                "Token",
                it.tokenType.capitalize().plus(" ").plus(it.accessToken)
            ).apply()
        }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }
}