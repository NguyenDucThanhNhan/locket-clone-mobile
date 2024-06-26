package com.myproject.locket_clone.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("USER_EMAIL")!!
        val password = intent.getStringExtra("USER_PASSWORD")!!


    }
}