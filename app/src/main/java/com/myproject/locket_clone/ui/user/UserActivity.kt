package com.myproject.locket_clone.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityUserBinding
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.ui.change_email.ChangeEmail_VerifyEmailActivity
import com.myproject.locket_clone.ui.change_email.ChangeEmail_VerifyPasswordActivity
import com.myproject.locket_clone.ui.change_name.ChangeNameActivity
import com.myproject.locket_clone.ui.sign_in.SignInActivity
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModel
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModelFactory
import com.squareup.picasso.Picasso

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { UserProfileViewModelFactory(repository) }
        val userProfileViewModel = ViewModelProvider(this, viewModelFactory).get(
            UserProfileViewModel::class.java)

        //Nhan du lieu tu HomeActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile

        //Gan du lieu vao layout
        if (userProfile != null) {
            Picasso.get().load(userProfile.profileImageUrl).into(binding.txtUserAvatar)
            binding.txtUserName.text = "${userProfile.lastname} ${userProfile.firstname}"
        }

        //Click edit infor
        binding.btnEditInfor.setOnClickListener {
            val intent = Intent(this, ChangeNameActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            startActivity(intent)
        }

        //Click sign out
        binding.btnSignOut.setOnClickListener {
            if (userProfile != null) {
                userProfileViewModel.signOut(userProfile.signInKey, userProfile.userId)
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }

        //Click change email
        binding.btnChangeEmailAddress.setOnClickListener {
            val intent = Intent(this, ChangeEmail_VerifyPasswordActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            startActivity(intent)
        }
    }
}