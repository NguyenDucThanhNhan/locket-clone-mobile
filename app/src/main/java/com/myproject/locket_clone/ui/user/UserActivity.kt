package com.myproject.locket_clone.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}