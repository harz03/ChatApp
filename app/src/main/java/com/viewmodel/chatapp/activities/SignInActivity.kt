package com.viewmodel.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.viewmodel.chatapp.R
import com.viewmodel.chatapp.databinding.ActivitySignInBinding
import com.viewmodel.chatapp.utilities.Constants
import com.viewmodel.chatapp.utilities.PrefrenceManger
import java.util.*
import kotlin.collections.HashMap

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var prefrenceManger:PrefrenceManger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefrenceManger = PrefrenceManger(applicationContext)
        binding.tvCreateNewAccount.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
        binding.btnSignIn.setOnClickListener {
            if(isValidSignUp()){
                signIn()
            }
        }
    }

    private fun signIn() {
        loding(true)
        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL,binding.etInputEmail.text.toString())
            .whereEqualTo(Constants.KEY_PASSWORD,binding.etInputPassword.text.toString())
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful && it.result != null && it.result.documents.size>0){
                    val documentSnapshot = it.result!!.documents.get(0)
                    prefrenceManger.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                    prefrenceManger.putString(Constants.KEY_USER_ID,documentSnapshot.id)
                    documentSnapshot.getString(Constants.KEY_NAME)?.let { it1 ->
                        prefrenceManger.putString(Constants.KEY_NAME,
                            it1
                        )
                    }
                    documentSnapshot.getString(Constants.KEY_IMAGE)?.let { it1 ->
                        prefrenceManger.putString(Constants.KEY_IMAGE,
                            it1
                        )
                    }
                    val intent = Intent(this,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }else{
                    loding(false)
                    showMessage("Unable to Sign In");
                }
            }
    }

    private fun loding(isLoading:Boolean) {
        if(isLoading){
            binding.btnSignIn.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnSignIn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showMessage(message:String){
        Toast.makeText(this,"$message",Toast.LENGTH_SHORT).show()
    }

    fun isValidSignUp():Boolean{
        if(binding.etInputEmail.text.toString().isEmpty()){
            showMessage("Enter Email")
            return false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etInputEmail.text.toString()).matches()){
            showMessage("Enter Valid Email")
            return false
        }else if(binding.etInputPassword.text.toString().isEmpty()){
            showMessage("Enter Password")
            return false
        }else {
            return true
        }
    }
}


