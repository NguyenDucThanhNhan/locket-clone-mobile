package com.myproject.locket_clone.ui.create_feed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.databinding.ActivityCreatePostBinding
import com.myproject.locket_clone.model.CreateFeedResponse
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.viewmodel.home.HomeViewModel
import com.myproject.locket_clone.viewmodel.home.HomeViewModelFactory
import com.squareup.picasso.Picasso
import java.io.File

class CreateFeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userProfile: UserProfile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { HomeViewModelFactory(repository) }
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(
            HomeViewModel::class.java)

        //Nhan du lieu tu SignInActivity
        userProfile = (intent.getSerializableExtra("USER_PROFILE") as? UserProfile)!!
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?

        val imagePath = intent.getStringExtra("IMAGE_PATH")
        val photoFile = File(imagePath)

        Picasso.get().load(photoFile).into(binding.imgPreview)

        binding.btnSend.setOnClickListener {
            uploadPhoto(photoFile)
        }

        // Lang nghe ket qua tra ve tu server sau khi tao bai viet
        homeViewModel.createFeedResponse.observe(this) { response ->
            handleCreateFeedResponse(response)
        }
    }

    private fun handleCreateFeedResponse(response: CreateFeedResponse) {
        when (response.status) {
            201 -> {
                val metadata = response.metadata ?: return
                // Xử lý metadata
                Log.d("CreateFeed", "Feed được tạo thành công: ${metadata.description}")
            }
            400 -> {
                // Xử lý các trạng thái lỗi khác
                Log.e("CreateFeed", "Lỗi khi tạo feed: ${response.message}")
            }
            else -> {
                // Xử lý lỗi không xác định
                Log.e("CreateFeed", "Lỗi không xác định: ${response.message}")
            }
        }
    }

    private fun uploadPhoto(photoFile: File) {

        homeViewModel.createFeed(
            authorization = userProfile.signInKey,
            userId = userProfile.userId,
            description = "This is my photo",
            visibility = "667bdce171b6ecf805a177a8",
            imageFile = photoFile,
        )
    }
}
