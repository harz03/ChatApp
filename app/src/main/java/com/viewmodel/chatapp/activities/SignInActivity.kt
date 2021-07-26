package com.viewmodel.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.viewmodel.chatapp.R
import com.viewmodel.chatapp.databinding.ActivitySignInBinding
import java.util.*
import kotlin.collections.HashMap

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvCreateNewAccount.setOnClickListener {
            startActivity(Intent(applicationContext,SignUpActivity::class.java))


        }
        binding.btnSignIn.setOnClickListener{
            addDatatoFirestore()
        }
    }

    private fun addDatatoFirestore() {
        val database = FirebaseFirestore.getInstance()
        val data = HashMap<String, Any>()
        data.put("first_name","Harshit")
        data.put("last_name","Gupta")
        Log.d("dataii","${data["last_name"]}")
        database.collection("users")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this,"Data Inserted",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"$it",Toast.LENGTH_SHORT).show()
            }
    }
}