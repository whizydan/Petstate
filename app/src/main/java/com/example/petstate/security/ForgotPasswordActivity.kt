package com.example.petstate.security

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.example.petstate.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val email=findViewById<EditText>(R.id.editTextTextTexttxttxtEmailAddress)
        when{
            TextUtils.isEmpty(email.text.toString().replace(" ".toRegex(),""))->{
                val toast=Toast.makeText(applicationContext,"Enter your email address",Toast.LENGTH_LONG)
                toast.show()
            }else->{
            val ResetEmail:String = email.text.toString().replace(" ".toRegex(),"")
            FirebaseAuth.getInstance().sendPasswordResetEmail(ResetEmail).addOnCompleteListener {
                    task->
                if(task.isSuccessful){
                    Toast.makeText(this@ForgotPasswordActivity,"Email sent", Toast.LENGTH_SHORT).show()
                }
            }
        }
        }
    }
}