package com.myproject.locket_clone.view.change_name

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.databinding.ActivityChangeNameBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.NameChangeResponse
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.view.user.UserActivity
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModel
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModelFactory

class ChangeNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeNameBinding
    private lateinit var friendList: ArrayList<Friend>
    private lateinit var sentInviteList: ArrayList<Friend>
    private lateinit var receivedInviteList: ArrayList<Friend>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { UserProfileViewModelFactory(repository) }
        val userProfileViewModel = ViewModelProvider(this, viewModelFactory).get(
            UserProfileViewModel::class.java)

        //Nhan du lieu tu UserActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        friendList = (intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?)!!
        sentInviteList = (intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?)!!
        receivedInviteList = (intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?)!!

        //Gan du lieu vao layout
        if (userProfile != null) {
            binding.edtFirstName.setText(userProfile.firstname)
            binding.edtLastName.setText(userProfile.lastname)
        }

        binding.btnSave.setOnClickListener {
            val firstname = binding.edtFirstName.text.toString()
            val lastname = binding.edtLastName.text.toString()
            val token = userProfile?.signInKey!!
            val userId = userProfile.userId

            userProfileViewModel.changeName(token, userId, firstname, lastname)
        }

        userProfileViewModel.nameChangeResponse.observe(this, Observer { response ->
            if (userProfile != null) {
                val firstname = binding.edtFirstName.text.toString()
                val lastname = binding.edtLastName.text.toString()
                handleNameChangeResponse(response, userProfile, firstname, lastname)
            }
        })
    }

    private fun handleNameChangeResponse(response: NameChangeResponse, userProfile: UserProfile, firstname: String, lastname: String) {
        if (response.status == 200) {
            val profileImageUrl = response.metadata?.profileImageUrl ?: userProfile.profileImageUrl
            val user = UserProfile(
                email = userProfile.email,
                userId = userProfile.userId,
                firstname = firstname,
                lastname = lastname,
                birthday = userProfile.birthday,
                profileImageUrl = profileImageUrl,
                signInKey = userProfile.signInKey,
            )
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("USER_PROFILE", user)
            intent.putExtra("FRIEND_LIST", friendList)
            intent.putExtra("SENT_INVITE_LIST", sentInviteList)
            intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            startActivity(intent)
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
        }
    }
}