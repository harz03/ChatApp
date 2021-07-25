package com.viewmodel.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.viewmodel.chatapp.R
import com.viewmodel.chatapp.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvCreateNewAccount.setOnClickListener {
            startActivity(Intent(applicationContext,SignUpActivity::class.java))
        }
    }
}