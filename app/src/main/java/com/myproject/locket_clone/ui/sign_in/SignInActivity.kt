package com.myproject.locket_clone.ui.sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.databinding.ActivitySignInBinding
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.repository.Repository
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

        binding.btnContinue.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your information!", Toast.LENGTH_LONG)
                return@setOnClickListener
            } else {
                signInViewModel.signin(email, password)
            }
        }

        signInViewModel.signinResponse.observe(this, Observer { response ->
            handleSigninResponse(response)
        })
    }

    private fun handleSigninResponse(response: SigninResponse) {
        when {
            response.status == 200 -> {
                val metadata = response.metadata ?: return
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("userId", metadata.user._id)
                    putExtra("email", metadata.user.email)
                    putExtra("fullName", "${metadata.user.fullname.firstname} ${metadata.user.fullname.lastname}")
                    putExtra("birthday", metadata.user.birthday)
                    putExtra("profileImageUrl", metadata.user.profileImageUrl)
                    putExtra("signInKey", metadata.signInKey)
                }
                startActivity(intent)
                finish()
            }
            response.status == 403 -> {
                val response_text = when (response.message) {
                    "Data is required" -> "Email or password is invalid"
                    "Email is not registered" -> "Email is not registered"
                    "Password is incorrect" -> "Password is incorrect"
                    else -> "Uncorrected username or password"
                }
                Toast.makeText(this, response_text, Toast.LENGTH_LONG).show()
            }
            else -> {
                val response_text = "Uncorrected username or password"
                Toast.makeText(this, response_text, Toast.LENGTH_LONG).show()
            }
        }
    }
}