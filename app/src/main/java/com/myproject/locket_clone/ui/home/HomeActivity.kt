package com.myproject.locket_clone.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityHomeBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.ui.friends.FriendsActivity
import com.myproject.locket_clone.ui.search_user.SearchUserActivity
import com.myproject.locket_clone.ui.sign_in.SignInActivity
import com.myproject.locket_clone.ui.user.UserActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Nhan du lieu tu SignInActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?

        //Gan du lieu vao layout
        if (friendList != null) {
            if (friendList.size != 1) {
                binding.btnFriends.text = "${friendList.size} Friends"
            } else {
                binding.btnFriends.text = "1 Friend"
            }
        }

        binding.btnUserProfile.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java).apply {
                putExtra("USER_PROFILE", userProfile)
                putExtra("FRIEND_LIST", friendList)
                putExtra("SENT_INVITE_LIST", sentInviteList)
                putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            }
            startActivity(intent)
        }

        binding.btnSearchUser.setOnClickListener {
            val intent = Intent(this, SearchUserActivity::class.java).apply {
                putExtra("USER_PROFILE", userProfile)
                putExtra("FRIEND_LIST", friendList)
                putExtra("SENT_INVITE_LIST", sentInviteList)
                putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            }
            startActivity(intent)
        }

        binding.btnFriends.setOnClickListener {
            val intent = Intent(this, FriendsActivity::class.java).apply {
                putExtra("USER_PROFILE", userProfile)
                putExtra("FRIEND_LIST", friendList)
                putExtra("SENT_INVITE_LIST", sentInviteList)
                putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            }
            startActivity(intent)
        }
    }
}