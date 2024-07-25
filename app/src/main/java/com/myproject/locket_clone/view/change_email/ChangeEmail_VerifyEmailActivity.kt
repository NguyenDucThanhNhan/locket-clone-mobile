package com.myproject.locket_clone.view.change_email

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityChangeEmailVerifyEmailBinding
import com.myproject.locket_clone.model.ChangeEmailResponse
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.view.user.UserActivity
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModel
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModelFactory

class ChangeEmail_VerifyEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeEmailVerifyEmailBinding
    private lateinit var friendList: ArrayList<Friend>
    private lateinit var sentInviteList: ArrayList<Friend>
    private lateinit var receivedInviteList: ArrayList<Friend>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeEmailVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { UserProfileViewModelFactory(repository) }
        val userProfileViewModel = ViewModelProvider(this, viewModelFactory).get(
            UserProfileViewModel::class.java)
        //code nhan tu server
        var trueCode: String = ""
        //email se gui server
        var userEmail: String = ""
        binding.edtEmail.setText("")
        binding.btnSave.setBackgroundResource(R.drawable.btn_gray_button)
        binding.btnSendCode.setBackgroundResource(R.drawable.btn_gray_button)
        binding.txtSendCode.visibility = View.GONE
        binding.txtVerification.visibility = View.GONE
        binding.edtVerification.visibility = View.GONE
        binding.btnSendCode.isEnabled = false
        binding.btnSave.isEnabled = false

        //Nhan du lieu tu ChangeEmail_VerifyPasswordActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        friendList = (intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?)!!
        sentInviteList = (intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?)!!
        receivedInviteList = (intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?)!!

        //Sư kien click send code -> gui email len server
        binding.btnSendCode.setOnClickListener {
            userEmail = binding.edtEmail.text.toString()
            val email = binding.edtEmail.text.toString()
            userProfileViewModel.validateEmail(email)
            binding.txtSendCode.visibility = View.VISIBLE
            binding.txtVerification.visibility = View.VISIBLE
            binding.edtVerification.visibility = View.VISIBLE
        }

        binding.btnSave.setOnClickListener {
            val code = binding.edtVerification.text.toString()
            //Kiem tra cac input co hop le hay khong
            if (userProfileViewModel.isValidSixDigitNumber(code)){
                if (userProfileViewModel.isValidCode(code, trueCode)){
                    val newEmail = binding.edtEmail.text.toString()
                    if (newEmail.isNotEmpty() && userProfile != null) {
                        //Doi email
                        userProfileViewModel.changeEmail(
                            userProfile.signInKey,
                            userProfile.userId,
                            newEmail,
                            onSuccess = { response ->
                                handleEmailChangeResponse(response, userProfile, newEmail)
                            },
                            onFailure = { errorMessage ->
                                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        )
                    } else {
                        Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Wrong code", Toast.LENGTH_LONG).show()
                }
            }
        }

        //Kiem tra dinh dang email co hop le hay khong
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val email = binding.edtEmail.text.toString()
                if (userProfileViewModel.isValidEmail(email)){
                    binding.btnSendCode.setBackgroundResource(R.drawable.btn_yellow_button)
                    binding.btnSendCode.isEnabled = true
                } else {
                    binding.btnSendCode.setBackgroundResource(R.drawable.btn_gray_button)
                    binding.btnSendCode.isEnabled = false
                }
            }
        })

        //Kiem tra dinh dang code co hop le hay khong
        binding.edtVerification.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val code = binding.edtVerification.text.toString()
                if (userProfileViewModel.isValidSixDigitNumber(code)){
                    binding.btnSave.setBackgroundResource(R.drawable.btn_yellow_button)
                    binding.btnSave.isEnabled = true
                } else {
                    binding.btnSave.setBackgroundResource(R.drawable.btn_gray_button)
                    binding.btnSave.isEnabled = false
                }
            }

        })

        //Xu ly ket qua tra ve tu server
        userProfileViewModel.emailValidationResponse.observe(this, Observer { response ->
            val responseText: String
            var checkStatus = false

            when {
                response.status == 400 -> {
                    responseText = when (response.message) {
                        "Email is registered" -> {
                            checkStatus = false
                            "Email is registered"
                        }
                        "Email is invalid" -> {
                            checkStatus = false
                            "Email is invalid"
                        }
                        else -> {
                            checkStatus = false
                            "Forbidden: ${response.message}"
                        }
                    }
                }
                response.status == 200 -> {
                    trueCode = response.metadata?.code.toString()
                    checkStatus = true
                    responseText = "Sent code successfully!"
                }
                else -> {
                    responseText = response.message ?: "Unknown error"
                    checkStatus = false
                }
            }

            if (!checkStatus) {
                Toast.makeText(this, responseText, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun handleEmailChangeResponse(response: ChangeEmailResponse, userProfile: UserProfile, newEmail: String) {
        if (response.status == 200) {
            val user = userProfile.copy(email = newEmail)
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