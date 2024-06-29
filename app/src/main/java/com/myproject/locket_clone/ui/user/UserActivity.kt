package com.myproject.locket_clone.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityUserBinding
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.ui.change_name.ChangeNameActivity
import com.squareup.picasso.Picasso

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}