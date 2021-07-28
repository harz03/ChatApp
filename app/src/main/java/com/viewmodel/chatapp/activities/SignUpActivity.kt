package com.viewmodel.chatapp.activities


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore

import com.viewmodel.chatapp.databinding.ActivitySignUpBinding
import com.viewmodel.chatapp.utilities.Constants
import com.viewmodel.chatapp.utilities.PrefrenceManger
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var encoded_image:String
    private lateinit var prefrenceManger: PrefrenceManger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefrenceManger = PrefrenceManger(applicationContext)
        binding.tvLogin.setOnClickListener{
            it->onBackPressed()
        }

        binding.btnSignUp.setOnClickListener{
            if(isSignUpValid()){
                signUp()
            }
        }
        binding.layoutImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickimage.launch(intent)
        }
    }

    private fun showMessage(message:String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    private fun signUp(){
            loading(true)
            val database = FirebaseFirestore.getInstance()
            val user = HashMap<String,Any>()
            user.put(Constants.KEY_NAME,binding.etInputName.text.toString())
            user.put(Constants.KEY_EMAIL,binding.etInputEmail.text.toString())
            user.put(Constants.KEY_PASSWORD,binding.etInputPassword.text.toString())
            user.put(Constants.KEY_IMAGE,encoded_image)
            database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener {
                    loading(false)
                    prefrenceManger.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                    prefrenceManger.putString(Constants.KEY_USER_ID,it.id)
                    prefrenceManger.putString(Constants.KEY_NAME,binding.etInputName.text.toString())
                    prefrenceManger.putString(Constants.KEY_IMAGE,encoded_image)
                    val intent=Intent(this,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }.addOnFailureListener{
                    loading(false)
                    it.message?.let { it1 -> showMessage(it1) }
                }
    }

    private fun encodeImage(bitmap: Bitmap):String{
        val previewWidth = 150;
        val previewHeight = bitmap.height*previewWidth/bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT)
    }

    private val pickimage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            if (it.data != null) {
                val imageUri = it.data!!.data
                try {
                    val inputStream = imageUri?.let { it1 -> contentResolver.openInputStream(it1) }
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.imageProfile.setImageBitmap(bitmap)
                    binding.tvAddImage.visibility = View.GONE
                    encoded_image = encodeImage(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private  fun isSignUpValid():Boolean{

        if(encoded_image == null){
            showMessage("Select Profile Image")
            return false
        }
        else if(binding.etInputName.text.toString().trim().isEmpty()){
            showMessage("Enter Name")
            return false
        }
        else if(binding.etInputEmail.text.toString().trim().isEmpty()){
            showMessage("Enter Email")
            return false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etInputEmail.text.toString().trim()).matches()){
            showMessage("Enter Valid Email")
            return false
        }else if(binding.etInputPassword.text.toString().isEmpty()){
            showMessage("Enter password")
            return false
        }else if(binding.etConfirmPassword.text.toString().isEmpty() || binding.etConfirmPassword.text
                .toString() != binding.etInputPassword.text.toString()){
            showMessage("Reenter password")
        }else{
            return true
        }
        return true
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading){
            binding.btnSignUp.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnSignUp.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}