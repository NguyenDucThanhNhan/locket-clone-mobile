package com.myproject.locket_clone.view.update_feed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.locket_clone.databinding.ActivityUpdateFeedBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.Fullname
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.model.Visibility
import com.myproject.locket_clone.recycler_view.VisibilityInFeedAdapter
import com.myproject.locket_clone.recycler_view.VisibilityInFeedInterface
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.viewmodel.feed.FeedViewModel
import com.myproject.locket_clone.viewmodel.feed.FeedViewModelFactory
import com.squareup.picasso.Picasso
import java.io.File

class UpdateFeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateFeedBinding
    private lateinit var userProfile: UserProfile
    private lateinit var visibilityAdapter: VisibilityInFeedAdapter
    private lateinit var feedViewModel: FeedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { FeedViewModelFactory(repository) }
        feedViewModel = ViewModelProvider(this, viewModelFactory).get(
            FeedViewModel::class.java)
        var visibility: String = ""
        var visibilityRootList: ArrayList<String> = ArrayList()

        //Nhan du lieu tu FeedActivity
        userProfile = (intent.getSerializableExtra("USER_PROFILE") as? UserProfile)!!
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?
        val feedId = intent.getStringExtra("FEED_ID")
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

        visibilityAdapter = VisibilityInFeedAdapter(visibilityList, object:
            VisibilityInFeedInterface {
            override fun onClickVisibility(position: Int) {
                //Check va them visibility vao
                if (!visibilityList[position].isClick) {

                } else {

                }
            }

        })
        binding.rvVisibilityFriend.adapter = visibilityAdapter

        binding.rvVisibilityFriend.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)
    }
}