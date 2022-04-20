package com.example.petstate.petowner.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.example.petstate.R;
import kotlinx.android.synthetic.main.activity_signingpost.*

class Signing : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signingpost)

        //Initialize Firebase Auth
        mAuth=FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()

        //if user logged in, go to sign in screen
        if(mAuth!!.currentUser!=null){
            startActivity(Intent(this,welcome1::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility= View.GONE
    }

    fun loginButtonClicked(view: View){
        if(TextUtils.isEmpty(emailsp.text.toString())){
            Toast.makeText(applicationContext,"Enter Username!", Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(passwordsp.text.toString())){
            Toast.makeText(applicationContext,"Enter password!", Toast.LENGTH_LONG).show()
            return
        }

        progressBar.visibility=View.VISIBLE

        //Authenticate user
        mAuth!!.signInWithEmailAndPassword(emailsp.text.toString(),passwordsp.text.toString())
            .addOnCompleteListener(this){task ->

                progressBar.visibility=View.GONE

                if(task.isSuccessful){
                    val intent=Intent(this,welcome1::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    if(passwordsp.text.toString().length<6){
                        passwordsp.error="Password is too short, enter minimum 6 characters"
                    }
                    Toast.makeText(this,"Authentication Failed"+task.exception,Toast.LENGTH_SHORT).show()
                }
            }
    }
}
