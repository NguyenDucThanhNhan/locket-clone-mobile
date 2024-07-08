package com.myproject.locket_clone.ui.sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.databinding.ActivitySignInBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.Fullname
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.ui.change_password.ChangePasswordActivity
import com.myproject.locket_clone.ui.home.HomeActivity
import com.myproject.locket_clone.viewmodel.sign_in.SignInViewModel
import com.myproject.locket_clone.viewmodel.sign_in.SignInViewModelFactory

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { SignInViewModelFactory(repository) }
        val signInViewModel = ViewModelProvider(this, viewModelFactory).get(
            SignInViewModel::class.java)

        //Nhan gia tr email va password
        var email = intent.getStringExtra("USER_EMAIL")
        var password = intent.getStringExtra("USER_PASSWORD")
        //Kiem tra co rong hay khong
        if (email == null || password == null) {
            email = ""
            password = ""
        }
        //Set e mail va password vao edt
        binding.edtEmail.setText(email)
        binding.edtPassword.setText(password)

        binding.btnForgotPassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnContinue.setOnClickListener {
            email = binding.edtEmail.text.toString()
            password = binding.edtPassword.text.toString()

            if (email!!.isEmpty() || password!!.isEmpty()) {
                Toast.makeText(this, "Please enter your information!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                signInViewModel.signin(email!!, password!!)
            }
        }

        signInViewModel.signinResponse.observe(this, Observer { response ->
            handleSigninResponse(response, password!!)
        })
    }

    private fun handleSigninResponse(response: SigninResponse, password: String) {
        when {
            response.status == 200 -> {
                val metadata = response.metadata ?: return

                val friendList: ArrayList<Friend> = ArrayList()
                val sentInviteList: ArrayList<Friend> = ArrayList()
                val receivedInviteList: ArrayList<Friend> = ArrayList()

                for (friendData in metadata.user.friendList) {
                    val id = friendData.id
                    val firstname = friendData.name.firstname
                    val lastname = friendData.name.lastname
                    val profileImageUrl = friendData.profileImageUrl

                    val friend = Friend(id, Fullname(firstname, lastname), profileImageUrl)
                    friendList.add(friend)
                }

                for (friendData in metadata.user.sentInviteList) {
                    val id = friendData.id
                    val firstname = friendData.name.firstname
                    val lastname = friendData.name.lastname
                    val profileImageUrl = friendData.profileImageUrl

                    val friend = Friend(id, Fullname(firstname, lastname), profileImageUrl)
                    sentInviteList.add(friend)
                }

                for (friendData in metadata.user.receivedInviteList) {
                    val id = friendData.id
                    val firstname = friendData.name.firstname
                    val lastname = friendData.name.lastname
                    val profileImageUrl = friendData.profileImageUrl

                    val friend = Friend(id, Fullname(firstname, lastname), profileImageUrl)
                    receivedInviteList.add(friend)
                }

                val userProfile = UserProfile(
                    email = metadata.user.email,
                    userId = metadata.user._id,
                    firstname = metadata.user.fullname.firstname,
                    lastname = metadata.user.fullname.lastname,
                    birthday = metadata.user.birthday,
                    profileImageUrl = metadata.user.profileImageUrl,
                    signInKey = metadata.signInKey,
                    password = password
                )

                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("USER_PROFILE", userProfile)
                    putExtra("FRIEND_LIST", friendList)
                    putExtra("SENT_INVITE_LIST", sentInviteList)
                    putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
                }
                startActivity(intent)
                finish()
            }
            response.status == 500 -> {
                val response_text = when (response.message) {
                    "Data is required" -> "Email or password is invalid"
                    "Email is not registered" -> "Email is not registered"
                    "Password is incorrect" -> "Password is incorrect"
                    else -> {
                        Log.d("DEBUG", response.message)
                        "Uncorrected username or password"
                    }
                }
                Toast.makeText(this, response_text, Toast.LENGTH_LONG).show()
            }
            else -> {
                val response_text = response.message
                Toast.makeText(this, response_text, Toast.LENGTH_LONG).show()
            }
        }
    }
}