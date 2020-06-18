package com.example.sumtwitter.main.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sumtwitter.R
import com.example.sumtwitter.main.MainActivity
import com.example.sumtwitter.utils.Constants.Companion.AT_SIGN
import com.example.sumtwitter.utils.ifNotNull
import com.example.sumtwitter.utils.showText
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity() as MainActivity
        activity.setActionBar(getString(R.string.profile_label))

        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        observeViewModel()

        ifNotNull(sharedPreferences.getString("token", ""), sharedPreferences.getString("screen_name", "")) {
                token, screenName ->
            viewModel.getUser(token, screenName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun observeViewModel() {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.user.observe(this, Observer {
            loader.visibility = View.GONE
            it?.let {
                profile_name.text = it.name
                profile_screen_name.text = AT_SIGN.plus(it.screenName)
                profile_description.text = it.description
                profile_user_since.text = getString(R.string.user_since, it.createdAt.substring(0,19))

                Glide.with(profile_picture)
                    .load(it.profileImage)
                    .apply(RequestOptions().placeholder(R.drawable.ic_placeholder))
                    .into(profile_picture)
            }
        })

        viewModel.error.observe(this, Observer {
            showText(requireContext(), getString(R.string.profile_error))
        })
    }

}
