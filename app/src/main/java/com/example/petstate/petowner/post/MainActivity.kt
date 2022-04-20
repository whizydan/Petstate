package com.example.petstate.petowner.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.petstate.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import com.example.petstate.R;
import kotlinx.android.synthetic.main.activity_mainpost.*

class MainActivity2 : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null
    private var mFirebaseDatabaseInstances: FirebaseDatabase?=null
    private var mFirebaseDatabase: DatabaseReference?=null

    private var userId:String?=null
    private var emailAddress:String?=null
    private var usertype:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpost)

        //Get Firebase Instances
        mAuth=FirebaseAuth.getInstance()
        mFirebaseDatabaseInstances= FirebaseDatabase.getInstance()

        //if already logged in go to sign in screen
        if(mAuth!!.currentUser!=null){
            startActivity(Intent(this,welcome1::class.java))
            finish()
        }
    }

    fun onLoginClicked(view: View) {
        startActivity(Intent(this,Signing::class.java))
        finish()
    }
    fun onRegisterClicked(view: View) {
        if(TextUtils.isEmpty(usernamepost.text.toString())){
            Toast.makeText(applicationContext,"Enter Username!",Toast.LENGTH_LONG).show()
        }
        if(TextUtils.isEmpty(emailpost.text.toString())){
            Toast.makeText(applicationContext,"Enter email address!",Toast.LENGTH_LONG).show()
        }
        if(TextUtils.isEmpty(passwordpost.text.toString())){
            Toast.makeText(applicationContext,"Enter password!",Toast.LENGTH_LONG).show()
        }
        if(passwordpost.text.toString().length<6){
            Toast.makeText(applicationContext,"Password is too short",Toast.LENGTH_LONG).show()
        }

        progressBar!!.visibility=View.VISIBLE

        //create user
        mAuth!!.createUserWithEmailAndPassword(emailpost.text.toString(),passwordpost.text.toString())
            .addOnCompleteListener(this){task ->
                Toast.makeText(this,"createUserWithEmail:onComplete"+task.isSuccessful,Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE

                if(task.isSuccessful){

                    mFirebaseDatabase=mFirebaseDatabaseInstances!!.getReference("users")
                    val user=FirebaseAuth.getInstance().currentUser

                    //add username, email to database
                    userId=user!!.uid
                    emailAddress=user.email
                    usertype = gettype.getText().toString()
                    val myUser=User(usernamepost.text.toString(),emailAddress!!,usertype!!)

                    mFirebaseDatabase!!.child(userId!!).setValue(myUser)

                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Authentication Failed"+task.exception,Toast.LENGTH_SHORT).show()
                    Log.e("MyTag",task.exception.toString())
                }
            }
    }
}
