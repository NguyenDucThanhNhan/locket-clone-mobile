package com.myproject.locket_clone.ui.sign_in

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityChangePasswordBinding
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.viewmodel.create_account.CreateAccountViewModel
import com.myproject.locket_clone.viewmodel.create_account.CreateAccountViewModelFactory
import com.myproject.locket_clone.viewmodel.sign_in.SignInViewModel
import com.myproject.locket_clone.viewmodel.sign_in.SignInViewModelFactory

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { SignInViewModelFactory(repository) }
        val signInViewModel =
            ViewModelProvider(this, viewModelFactory).get(SignInViewModel::class.java)
        var userEmail: String = ""
        binding.edtEmail.setText("")
        binding.btnContinue.setBackgroundResource(R.drawable.btn_gray_button)
        binding.btnSendCode.setBackgroundResource(R.drawable.btn_gray_button)
        binding.txtSendCode.visibility = View.GONE
        binding.txtVerification.visibility = View.GONE
        binding.edtVerification.visibility = View.GONE
        binding.btnSendCode.isEnabled = false
        binding.btnContinue.isEnabled = false

        //Sư kien click send code -> gui email len server
        binding.btnSendCode.setOnClickListener {
            userEmail = binding.edtEmail.text.toString()
            val email = binding.edtEmail.text.toString()
            signInViewModel.validateEmail(email)
            binding.txtSendCode.visibility = View.VISIBLE
            binding.txtVerification.visibility = View.VISIBLE
            binding.edtVerification.visibility = View.VISIBLE
        }
    }
}