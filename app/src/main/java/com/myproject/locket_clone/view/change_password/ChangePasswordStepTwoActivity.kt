package com.myproject.locket_clone.view.change_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.databinding.ActivityChangePasswordStepTwoBinding
import com.myproject.locket_clone.model.ChangePasswordResponse
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.view.sign_in.SignInActivity
import com.myproject.locket_clone.viewmodel.sign_in.SignInViewModel
import com.myproject.locket_clone.viewmodel.sign_in.SignInViewModelFactory

class ChangePasswordStepTwoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordStepTwoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordStepTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { SignInViewModelFactory(repository) }
        val signInViewModel = ViewModelProvider(this, viewModelFactory).get(
            SignInViewModel::class.java)

        //Nhan email tu ChangePasswordActivity
        val userEmail = intent.getStringExtra("USER_EMAIL")!!
        var password = intent.getStringExtra("USER_EMAIL")!!

        binding.btnContinue.setOnClickListener {
            password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            if (signInViewModel.isValidPassword(password)
                && signInViewModel.isValidConfirmPassword(password, confirmPassword)){
                signInViewModel.changePassword(userEmail, password)

            } else {
                Toast.makeText(this, "Invalid password or confirm password", Toast.LENGTH_SHORT).show()
            }
        }

        signInViewModel.changePasswordResponse.observe(this, Observer { response ->
            handleChangePasswordResponse(response, userEmail, password)
        })
    }

    private fun handleChangePasswordResponse(
        response: ChangePasswordResponse?,
        userEmail: String,
        password: String
    ) {
        if (response != null) {
            when {
                response.status == 200 -> {
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.putExtra("USER_EMAIL", userEmail)
                    intent.putExtra("USER_PASSWORD", password)
                    startActivity(intent)
                }
                response.status == 400 -> {
                    Toast.makeText(this, "Data is invalid", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}