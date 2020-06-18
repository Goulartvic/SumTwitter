package com.example.sumtwitter.main.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sumtwitter.R
import com.example.sumtwitter.model.Tweet
import com.example.sumtwitter.utils.Constants.Companion.AT_SIGN
import kotlinx.android.synthetic.main.item_tweet.view.*

class TimelineAdapter : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tweet: Tweet) {
            itemView.tweet_name.text = tweet.user.name
            itemView.tweet_screen_name.text = AT_SIGN.plus(tweet.user.screenName)
            itemView.tweet_text.text = tweet.text
            itemView.tweet_date.text = tweet.createdAt.substring(0, 19)

            Glide.with(itemView)
                .load(tweet.user.profileImage)
                .apply(RequestOptions().placeholder(R.drawable.ic_placeholder))
                .into(itemView.profile_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    var data: List<Tweet> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
}