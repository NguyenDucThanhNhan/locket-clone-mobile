package com.myproject.locket_clone.ui.change_password

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
import com.myproject.locket_clone.databinding.ActivityChangePasswordBinding
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.ui.create_account.ChoosePasswordActivity
import com.myproject.locket_clone.ui.sign_in.SignInActivity
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
        var trueCode: String = ""
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

        binding.btnContinue.setOnClickListener {
            val code = binding.edtVerification.text.toString()
            if (signInViewModel.isValidSixDigitNumber(code)){
                if (signInViewModel.isValidCode(code, trueCode)){
                    val intent = Intent(this, ChangePasswordStepTwoActivity::class.java)
                    intent.putExtra("USER_EMAIL", userEmail)
                    startActivity(intent)
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
                if (signInViewModel.isValidEmail(email)){
                    binding.btnSendCode.setBackgroundResource(R.drawable.btn_yellow_button)
                    binding.btnSendCode.isEnabled = true
                } else {
                    binding.btnSendCode.setBackgroundResource(R.drawable.btn_gray_button)
                    binding.btnSendCode.isEnabled = false
                }
            }

        })

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        //Kiem tra dinh dang code co hop le hay khong
        binding.edtVerification.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val code = binding.edtVerification.text.toString()
                if (signInViewModel.isValidSixDigitNumber(code)){
                    binding.btnContinue.setBackgroundResource(R.drawable.btn_yellow_button)
                    binding.btnContinue.isEnabled = true
                } else {
                    binding.btnContinue.setBackgroundResource(R.drawable.btn_gray_button)
                    binding.btnContinue.isEnabled = false
                }
            }

        })

        //Xu ly ket qua tra ve tu server
        signInViewModel.emailValidationResponse.observe(this, Observer { response ->
            val responseText: String
            var checkStatus = false

            when {
                response.status == 403 -> {
                    responseText = when (response.message) {
                        "Email is not registered" -> {
                            checkStatus = false
                            "Email is not registered"
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
//                    responseText = "Sent code successfully: $trueCode"
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
}