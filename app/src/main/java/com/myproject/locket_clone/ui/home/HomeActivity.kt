package com.myproject.locket_clone.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityHomeBinding
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.ui.user.UserActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Nhan du lieu tu SignInActivity
        val email = intent.getStringExtra("email")!!
        val userId = intent.getStringExtra("userId")!!
        val firstname = intent.getStringExtra("firstname")!!
        val lastname = intent.getStringExtra("lastname")!!
        val birthday = intent.getStringExtra("birthday")!!
        val profileImageUrl = intent.getStringExtra("profileImageUrl")!!
        val signInKey = intent.getStringExtra("signInKey")!!

        val userProfile = UserProfile(
            userId = userId,
            email = email,
            firstname = firstname,
            lastname = lastname,
            birthday = birthday,
            profileImageUrl = profileImageUrl,
            signInKey = signInKey)


        binding.btnUserProfile.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java).apply {
                putExtra("USER_PROFILE", userProfile)
            }
            startActivity(intent)
        }
    }
}