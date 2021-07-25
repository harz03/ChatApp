package com.viewmodel.chatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.viewmodel.chatapp.R
import com.viewmodel.chatapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener{
            it->onBackPressed()
        }
    }
}