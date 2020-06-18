package com.example.sumtwitter.main.timeline

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumtwitter.R
import com.example.sumtwitter.utils.ifNotNull
import kotlinx.android.synthetic.main.fragment_timeline.*

class TimelineFragment : Fragment() {

    private lateinit var viewModel: TimelineViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private val adapter by lazy { TimelineAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            sharedPreferences = it.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        }

        observeViewModel()

        ifNotNull(sharedPreferences.getString("token", ""), sharedPreferences.getString("screen_name", "")) {
            token, screenName ->
            viewModel.getTweets(token, screenName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    private fun observeViewModel() {
        viewModel = ViewModelProvider(this)[TimelineViewModel::class.java]

        viewModel.tweetList.observe(this, Observer {
            loader.visibility = View.GONE
            recycler_view.visibility = View.VISIBLE

            recycler_view.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext())
            layoutManager.orientation = RecyclerView.VERTICAL
            recycler_view.layoutManager = layoutManager
            it?.let {
                adapter.data = it
            }
        })
    }


}
