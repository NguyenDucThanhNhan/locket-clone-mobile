package com.myproject.locket_clone.view.change_email

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.myproject.locket_clone.databinding.ActivityChangeEmailVerifyPasswordBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.view.sign_in.SignInActivity
import com.myproject.locket_clone.view.user.UserActivity

class ChangeEmail_VerifyPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeEmailVerifyPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeEmailVerifyPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Nhan du lieu tu UserActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?
        if (userProfile == null) {
            Toast.makeText(this, "Missing user profile", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnContinue.setOnClickListener {
            val password = binding.edtPassword.text.toString()
            if (userProfile != null) {
                if (password == userProfile.password) {
                    val intent = Intent(this, ChangeEmail_VerifyEmailActivity::class.java)
                    intent.putExtra("USER_PROFILE", userProfile)
                    intent.putExtra("FRIEND_LIST", friendList)
                    intent.putExtra("SENT_INVITE_LIST", sentInviteList)
                    intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
                    startActivity(intent)
                }
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            intent.putExtra("FRIEND_LIST", friendList)
            intent.putExtra("SENT_INVITE_LIST", sentInviteList)
            intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            startActivity(intent)
        }
    }
}