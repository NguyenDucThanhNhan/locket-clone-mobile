package com.myproject.locket_clone.ui.feed

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.myproject.locket_clone.databinding.ActivityFeedBinding
import com.myproject.locket_clone.model.Feed
import com.myproject.locket_clone.model.FeedMetadata
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.Fullname
import com.myproject.locket_clone.model.GetCertainFeedsResponse
import com.myproject.locket_clone.model.Reaction
import com.myproject.locket_clone.model.ReactionStatistic
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.recycler_view.FeedAdapter
import com.myproject.locket_clone.recycler_view.FeedInterface
import com.myproject.locket_clone.recycler_view.FriendsListAdapter
import com.myproject.locket_clone.recycler_view.FriendsListInterface
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.viewmodel.feed.FeedViewModel
import com.myproject.locket_clone.viewmodel.feed.FeedViewModelFactory

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var feedAdapter: FeedAdapter
    private var feedList = ArrayList<Feed>()
    private var friendList = ArrayList<Friend>()
    private lateinit var feedViewModel: FeedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { FeedViewModelFactory(repository) }
        feedViewModel = ViewModelProvider(this, viewModelFactory).get(
            FeedViewModel::class.java)

        //Nhan du lieu tu HomeActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>

        //Lay tat ca feed cho user
        if (userProfile != null) {
            feedViewModel.getCertainFeeds(userProfile.signInKey, userProfile.userId, userProfile.userId, 1)
        }

        //Xu ly ket qua tra ve tu server
        feedViewModel.getCertainFeedsResponse.observe(this) { response ->
            handleGetCertainFeedsResponse(response)
        }

        //Hien thi feeds
        feedAdapter = FeedAdapter(feedList, object: FeedInterface {
            override fun onClickHeart(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickHaha(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickLike(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickWow(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickAngry(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickSad(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickUserProfile(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickAllFriends(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickSearchUser(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickGrid(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickMore(position: Int) {
                TODO("Not yet implemented")
            }

        })
        binding.rvFeed.adapter = feedAdapter

        binding.rvFeed.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false)

        // Sử dụng SnapHelper để tạo hiệu ứng lướt
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvFeed)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleGetCertainFeedsResponse(response: GetCertainFeedsResponse) {
        when (response.status) {
            200 -> {
                val feeds = response.metadata ?: return
                // Xử lý danh sách feed
                feeds.forEach { feed ->
                    val feedId = feed._id
                    val userId = feed.userId
                    val description = feed.description
                    val profileImageUrl = feed.imageUrl
                    val createdAt = feedViewModel.formatDateString(feed.createdAt)
                    val updatedAt = feed.updatedAt
                    val v = feed.__v
                    val reactionStatistic = ReactionStatistic(feed.reactionStatistic.angry, feed.reactionStatistic.haha,
                        feed.reactionStatistic.like, feed.reactionStatistic.love, feed.reactionStatistic.sad, feed.reactionStatistic.wow)

                    val reactions =ArrayList<Reaction>()
                    for (r in feed.reactions) {
                        val userIdReaction = r.userId
                        val reactionFullname = Fullname(r.fullname.firstname, r.fullname.lastname)
                        val reactionImageUrl = r.profileImageUrl
                        val reactionIcon = r.icon
                        reactions.add(Reaction(userIdReaction, reactionFullname, reactionImageUrl, reactionIcon))
                    }

                    val visibility = ArrayList<String>()
                    if (feed.visibility is List<*>) {
                        (feed.visibility as List<*>).forEach {
                            visibility.add(it.toString())
                        }
                    } else if (feed.visibility is String) {
                        visibility.add(feed.visibility as String)
                    }

                    var name: String = ""
                    for (n in friendList) {
                        if (n.id == userId) {
                            name = n.name.firstname + " " + n.name.lastname
                            break
                        }
                    }

                    feedList.add(Feed(userId, description, profileImageUrl, visibility, feedId, reactions, reactionStatistic, createdAt, updatedAt, v, name))

                    feedAdapter.notifyDataSetChanged()
                }
            }
            400 -> {
                // Xử lý các trạng thái lỗi khác
                Log.e("GetCertainFeeds", "Lỗi khi lấy feed: ${response.message}")
            }
            else -> {
                // Xử lý lỗi không xác định
                Log.e("GetCertainFeeds", "Lỗi không xác định: ${response.message}")
            }
        }
    }
}