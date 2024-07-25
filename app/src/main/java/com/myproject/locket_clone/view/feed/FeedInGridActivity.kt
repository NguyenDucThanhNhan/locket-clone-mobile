package com.myproject.locket_clone.view.feed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityFeedInGridBinding
import com.myproject.locket_clone.model.Feed
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.recycler_view.FeedInGridAdapter
import com.myproject.locket_clone.recycler_view.FriendsListAdapter
import com.myproject.locket_clone.recycler_view.FriendsListInterface

class FeedInGridActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedInGridBinding
    private lateinit var feedInGridAdapter: FeedInGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedInGridBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Nhan du lieu tu FeedActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>
        val receivedInviteList =
            intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>
        val feedList = intent.getSerializableExtra("FEED_LIST") as ArrayList<Feed>

        feedInGridAdapter = FeedInGridAdapter(feedList)
        binding.rvFeedInGrid.adapter = feedInGridAdapter

        binding.rvFeedInGrid.layoutManager = GridLayoutManager(
            this,
            3,
            GridLayoutManager.VERTICAL,
            false)
    }
}