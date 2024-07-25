package com.myproject.locket_clone.view.create_account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.databinding.ActivityChoosePasswordBinding
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.viewmodel.create_account.CreateAccountViewModelFactory
import com.myproject.locket_clone.viewmodel.create_account.CreateAccountViewModel

class ChoosePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoosePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { CreateAccountViewModelFactory(repository) }
        val createAccountViewModel = ViewModelProvider(this, viewModelFactory).get(CreateAccountViewModel::class.java)

        //Nhan email tu ChooseEmailActivity
        val userEmail = intent.getStringExtra("USER_EMAIL")

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, ChooseEmailActivity::class.java)
            startActivity(intent)
        }

        binding.btnContinue.setOnClickListener {
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            if (createAccountViewModel.isValidPassword(password)
                && createAccountViewModel.isValidConfirmPassword(password, confirmPassword)){
                val intent = Intent(this, ChooseNameActivity::class.java)
                intent.putExtra("USER_EMAIL", userEmail)
                intent.putExtra("USER_PASSWORD", password)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid password or confirm password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}