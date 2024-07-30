package com.myproject.locket_clone.view.update_feed

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.locket_clone.databinding.ActivityUpdateFeedBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.Fullname
import com.myproject.locket_clone.model.UpdateFeedResponse
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.model.Visibility
import com.myproject.locket_clone.recycler_view.VisibilityInFeedAdapter
import com.myproject.locket_clone.recycler_view.VisibilityInFeedInterface
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.view.feed.FeedActivity
import com.myproject.locket_clone.view.feed.FeedInGridActivity
import com.myproject.locket_clone.viewmodel.feed.FeedViewModel
import com.myproject.locket_clone.viewmodel.feed.FeedViewModelFactory
import com.squareup.picasso.Picasso
import java.io.File

class UpdateFeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateFeedBinding
    private lateinit var userProfile: UserProfile
    private lateinit var visibilityAdapter: VisibilityInFeedAdapter
    private lateinit var feedViewModel: FeedViewModel
    private var friendList: ArrayList<Friend> = ArrayList()
    private var sentInviteList: ArrayList<Friend> = ArrayList()
    private var receivedInviteList: ArrayList<Friend> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { FeedViewModelFactory(repository) }
        feedViewModel = ViewModelProvider(this, viewModelFactory).get(
            FeedViewModel::class.java)
        //Day la list de gui len server cap nhat visibility
        var visibilityUpdate: String = ""
        //Day la list visibility cua feed ban dau
        var visibilityRootList: ArrayList<String> = ArrayList()

        //Nhan du lieu tu FeedActivity
        userProfile = (intent.getSerializableExtra("USER_PROFILE") as? UserProfile)!!
        friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>
        sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>
        receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>
        val feedId = intent.getStringExtra("FEED_ID")!!
        val description = intent.getStringExtra("DESCRIPTION")
        var visibilityRoot = intent.getStringExtra("VISIBILITY")
        val imagePath = intent.getStringExtra("IMAGE_PATH")!!

        //Gan du lieu vao layout
        binding.edtDescription.setText(description)
        Picasso.get().load(imagePath).into(binding.imgPreview)


        //Tao visibilityRootList de xem feed ban dau sẽ co ai duoc xem
        if (visibilityRoot != null) {
            // Loại bỏ dấu ngoặc vuông và dấu cách
            visibilityRoot = visibilityRoot.removeSurrounding("[", "]").trim()

            // Tạo ArrayList từ mảng
            visibilityRootList = ArrayList(visibilityRoot.split(", "))
        } else {
            visibilityRootList.clear()
        }

        //Tao list visibility va chuoi visibility
        //visibilityList la list dung de bo vao recyclerView
        val visibilityList: ArrayList<Visibility> = ArrayList()
        for (f in friendList) {
            val id = f.id
            val firstname = f.name.firstname
            val lastname = f.name.lastname
            val profileImageUrl = f.profileImageUrl
            val friend = Visibility(id, Fullname(firstname, lastname), profileImageUrl, false)
            visibilityList.add(friend)
        }

        //Kiem tra xem ai da nam trong visibility list truoc do
        if (visibilityRoot != null) {
            for (v in visibilityRootList) {
                for (f in visibilityList) {
                    if (v == f.id) {
                        f.isClick = true
                    }
                }
            }
        }

        //Tao visibilityUpdate
        for (v in visibilityRootList) {
            if (visibilityUpdate.isEmpty()) {
                visibilityUpdate = v
//                Log.d("DEBUG", visibilityUpdate)

            } else {
                visibilityUpdate += ", $v"

            }
        }


        visibilityAdapter = VisibilityInFeedAdapter(visibilityList, object:
            VisibilityInFeedInterface {
            override fun onClickVisibility(position: Int) {
                if (!visibilityList[position].isClick) {
                    //Khi chua tao visibility
                    if (visibilityUpdate == "everyone" || visibilityUpdate == "") {
                        // Tao chuoi visibility de gui
                        visibilityUpdate = visibilityList[position].id
                        // Doi mau cua nut everyone
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        // Cap nhat mau vien cua item
                        visibilityAdapter.updateBorderColor(position)
                    } else {
                        visibilityUpdate += ", " + visibilityList[position].id
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        visibilityAdapter.updateBorderColor(position)
                    }
                } else {
                    if (visibilityUpdate.contains(", ${visibilityList[position].id}")) {
                        visibilityUpdate = visibilityUpdate.replace(", ${visibilityList[position].id}", "")
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        visibilityAdapter.updateBorderColor(position)
                    } else if (visibilityUpdate.contains("${friendList[position].id}, ")) {
                        visibilityUpdate = visibilityUpdate.replace("${visibilityList[position].id}, ", "")
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        visibilityAdapter.updateBorderColor(position)
                    } else if (visibilityUpdate.contains(visibilityList[position].id)){
                        visibilityUpdate = visibilityUpdate.replace(visibilityList[position].id, "")
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        visibilityAdapter.updateBorderColor(position)
                    }
                }
            }

        })
        binding.rvVisibilityFriend.adapter = visibilityAdapter

        binding.rvVisibilityFriend.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)

        binding.imgEveryone.setOnClickListener {
            visibilityUpdate = "everyone"
            visibilityAdapter.resetAllItems()
            binding.imgEveryone.borderColor = Color.parseColor("#E3A400")
        }

        binding.btnSend.setOnClickListener {
            val descriptionUpdate = binding.edtDescription.text.toString()
            feedViewModel.updateFeed(userProfile.signInKey, userProfile.userId, feedId, descriptionUpdate, visibilityUpdate)
        }

        //Lang nghe ket qua tra ve tu server
        feedViewModel.updateFeedResponse.observe(this) { response ->
            handleUpdateFeedResponse(response)
        }
    }

    private fun handleUpdateFeedResponse(response: UpdateFeedResponse) {
        when (response.status) {
            200 -> {
                Log.d("UpdateFeed", "Updated feed successfully")
                val intent = Intent(this, FeedActivity::class.java).apply {
                    putExtra("USER_PROFILE", userProfile)
                    putExtra("FRIEND_LIST", friendList)
                    putExtra("SENT_INVITE_LIST", sentInviteList)
                    putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
                }
                startActivity(intent)
            }
            400 -> {
                // Xử lý các trạng thái lỗi khác
                Log.e("UpdateFeed", "Lỗi khi cập nhật feed: ${response.message}")
            }
            else -> {
                // Xử lý lỗi không xác định
                Log.e("UpdateFeed", "Lỗi không xác định: ${response.message}")
            }
        }
    }
}