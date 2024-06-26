package com.myproject.locket_clone.ui.create_account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityChooseEmailBinding
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.viewmodel.CreateAccountViewModelFactory
import com.myproject.locket_clone.viewmodel.create_account.CreateAccountViewModel

class ChooseEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { CreateAccountViewModelFactory(repository) }
        val createAccountViewModel =ViewModelProvider(this, viewModelFactory).get(CreateAccountViewModel::class.java)
        //code nhan tu server
        var trueCode: String = ""
        //email se gui server
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
            createAccountViewModel.validateEmail(email)
            binding.txtSendCode.visibility = View.VISIBLE
            binding.txtVerification.visibility = View.VISIBLE
            binding.edtVerification.visibility = View.VISIBLE
        }

        binding.btnContinue.setOnClickListener {
            val code = binding.edtVerification.text.toString()
            if (createAccountViewModel.isValidSixDigitNumber(code)){
                if (createAccountViewModel.isValidCode(code, trueCode)){
                    val intent = Intent(this, ChoosePasswordActivity::class.java)
                    intent.putExtra("USER_EMAIL", userEmail)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Wrong code", Toast.LENGTH_LONG).show()
                }
            }
        }

        //Kiem tra dinh dang email co hop le hay khong
        binding.edtEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val email = binding.edtEmail.text.toString()
                if (createAccountViewModel.isValidEmail(email)){
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
                if (createAccountViewModel.isValidSixDigitNumber(code)){
                    binding.btnContinue.setBackgroundResource(R.drawable.btn_yellow_button)
                    binding.btnContinue.isEnabled = true
                } else {
                    binding.btnContinue.setBackgroundResource(R.drawable.btn_gray_button)
                    binding.btnContinue.isEnabled = false
                }
            }

        })

        //Xu ly ket qua tra ve tu server
        createAccountViewModel.emailValidationResponse.observe(this, Observer { response ->
            val responseText: String
            var checkStatus = false

            when {
                response.status == 403 -> {
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
                    responseText = "Sent code successfully: $trueCode"
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