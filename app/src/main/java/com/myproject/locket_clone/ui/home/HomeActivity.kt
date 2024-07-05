package com.myproject.locket_clone.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityHomeBinding
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.ui.sign_in.SignInActivity
import com.myproject.locket_clone.ui.user.UserActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Nhan du lieu tu SignInActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile

        binding.btnUserProfile.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java).apply {
                putExtra("USER_PROFILE", userProfile)
            }
            startActivity(intent)
        }
    }
}