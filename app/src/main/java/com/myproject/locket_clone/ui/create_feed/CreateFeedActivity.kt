package com.myproject.locket_clone.ui.create_feed

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityCreateFeedBinding
import com.myproject.locket_clone.model.CreateFeedResponse
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.Fullname
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.model.Visibility
import com.myproject.locket_clone.recycler_view.FriendsListAdapter
import com.myproject.locket_clone.recycler_view.FriendsListInterface
import com.myproject.locket_clone.recycler_view.VisibilityFriendsAdapter
import com.myproject.locket_clone.recycler_view.VisibilityFriendsInterface
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.ui.friends.FriendsActivity
import com.myproject.locket_clone.ui.home.HomeActivity
import com.myproject.locket_clone.viewmodel.home.HomeViewModel
import com.myproject.locket_clone.viewmodel.home.HomeViewModelFactory
import com.squareup.picasso.Picasso
import java.io.File

class CreateFeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateFeedBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userProfile: UserProfile
    private lateinit var visibilityAdapter: VisibilityFriendsAdapter
    private var visibility: String = "everyone"

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { HomeViewModelFactory(repository) }
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(
            HomeViewModel::class.java)

        //Nhan du lieu tu SignInActivity
        userProfile = (intent.getSerializableExtra("USER_PROFILE") as? UserProfile)!!
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?

        //Tao list visibility
        val visibilityList: ArrayList<Visibility> = ArrayList()
        for (friendData in friendList) {
            val id = friendData.id
            val firstname = friendData.name.firstname
            val lastname = friendData.name.lastname
            val profileImageUrl = friendData.profileImageUrl

            val friend = Visibility(id, Fullname(firstname, lastname), profileImageUrl, false)
            visibilityList.add(friend)
        }

        val imagePath = intent.getStringExtra("IMAGE_PATH")
        val photoFile = File(imagePath)

        Picasso.get().load(photoFile).into(binding.imgPreview)

        //Load visibility
        visibilityAdapter = VisibilityFriendsAdapter(visibilityList, object: VisibilityFriendsInterface {
            override fun OnClickVisibility(position: Int) {
                //Check va them visibility vao
                if (!visibilityList[position].isClick) {
                    if (visibility == "everyone" || visibility == "") {
                        // Tao chuoi visibility de gui
                        visibility = visibilityList[position].id
                        // Doi mau cua nut everyone
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        // Cap nhat mau vien cua item
                        visibilityAdapter.updateBorderColor(position)
                    } else {
                        visibility += ", " + visibilityList[position].id
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        visibilityAdapter.updateBorderColor(position)
                    }
                } else {
                    if (visibility.contains(", ${visibilityList[position].id}")) {
                        visibility = visibility.replace(", ${visibilityList[position].id}", "")
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        visibilityAdapter.updateBorderColor(position)
                    } else if (visibility.contains("${friendList[position].id}, ")) {
                        visibility = visibility.replace("${visibilityList[position].id}, ", "")
                        binding.imgEveryone.borderColor = Color.parseColor("#595959")
                        visibilityAdapter.updateBorderColor(position)
                    } else if (visibility.contains(visibilityList[position].id)){
                        visibility = visibility.replace(visibilityList[position].id, "")
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
            visibility = "everyone"
            visibilityAdapter.resetAllItems()
            binding.imgEveryone.borderColor = Color.parseColor("#E3A400")
            //
            Log.d("DEBUG", visibility)
        }

        binding.btnSend.setOnClickListener {
            uploadPhoto(photoFile)
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_PROFILE", userProfile)
                putExtra("FRIEND_LIST", friendList)
                putExtra("SENT_INVITE_LIST", sentInviteList)
                putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            }
            startActivity(intent)
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
            description = binding.edtDescription.text.toString(),
            visibility = visibility,
            imageFile = photoFile
        )
    }
}
