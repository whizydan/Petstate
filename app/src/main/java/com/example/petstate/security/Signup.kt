package com.example.petstate.security



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.MainActivity
import com.example.petstate.R
import com.example.petstate.petowner.RecordListActivity
import com.example.petstate.petowner.post.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private var userId:String?=null
    private var emailAddress:String?=null

    val TAG = "welcome"


    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val email = findViewById<EditText>(R.id.Email2)
        val password = findViewById<EditText>(R.id.editPassword1)
        val btn = findViewById<Button>(R.id.btnReg)
        val textField = findViewById<TextInputLayout>(R.id.dropdown)
        val items = listOf("vet","pet owner")
        val adapter = ArrayAdapter(applicationContext,R.layout.userlisttype,items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        //vet list data
        val vet_name = findViewById<EditText>(R.id.PersonName)
        val vet_add = findViewById<EditText>(R.id.Address)
        val phone = findViewById<EditText>(R.id.phone)


        btn.setOnClickListener {

            when {
                TextUtils.isEmpty(email.text.toString().replace(" ".toRegex(), "")) -> {
                    Toast.makeText(
                        this, "please enter email", Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty(vet_add.text.toString().replace(" ".toRegex(), "")) -> (
                        Toast.makeText(
                            this, "please enter an address", Toast.LENGTH_SHORT
                        ).show()
                        )

                TextUtils.isEmpty(phone.text.toString().replace(" ".toRegex(), "")) -> (
                        Toast.makeText(
                            this, "please enter an phone", Toast.LENGTH_SHORT
                        ).show()
                        )


                TextUtils.isEmpty((textField.editText as? AutoCompleteTextView)?.editableText.toString().replace(" ".toRegex(), "")) -> {
                    textField.setError("please fill user type")
                    Toast.makeText(
                        this@Signup, "please select user type", Toast.LENGTH_SHORT
                    ).show()

                }

                TextUtils.isEmpty(email.text.toString().replace(" ".toRegex(), "")) -> {
                    Toast.makeText(
                        this@Signup, "please enter email", Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty(password.text.toString().replace(" ".toRegex(), "")) -> {
                    Toast.makeText(
                        this@Signup, "please enter a password", Toast.LENGTH_SHORT
                    ).show()

                }
                else -> {

                    val pass = password.getText().toString()
                    val email = email.getText().toString()
                    val phonenumber = phone.getText().toString()
                    val type = (textField.editText as? AutoCompleteTextView)?.editableText.toString()
                    mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val usertype = (textField.editText as? AutoCompleteTextView)?.editableText.toString()
                                login(email,vet_name.text.toString(),pass,usertype)
                            } else {
                                Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG)
                                    .show()
                            }
                        })
                }

            }
        }
    }
    private fun login(email:String,name:String,password:String,usertype:String){
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                var mFirebaseDatabaseInstances= FirebaseDatabase.getInstance()
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG)
                        .show()
                    if (usertype == "pet owner"){
                        var mFirebaseDatabase=mFirebaseDatabaseInstances.getReference("users/$usertype")
                        val user=FirebaseAuth.getInstance().currentUser
                        userId=user!!.uid
                        emailAddress=user.email
                        val myUser= User(name,emailAddress!!,usertype)
                        mFirebaseDatabase.child(userId!!).setValue(myUser).addOnCompleteListener(this,
                            OnCompleteListener { task ->
                                if (task.isSuccessful){
                                    val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
                                    var editor = sharedPreferences.edit()
                                    editor.putString("type",usertype)
                                    editor.commit()
                                    Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this,RecordListActivity::class.java))
                                    finish()
                                }else {
                                    Toast.makeText(this, "Data save Failed", Toast.LENGTH_LONG).show()
                                }
                            })
                    }else if (usertype == "vet"){
                        var mFirebaseDatabase=mFirebaseDatabaseInstances.getReference("users/$usertype")
                        val user=FirebaseAuth.getInstance().currentUser
                        userId=user!!.uid
                        emailAddress=user.email
                        val myUser= User(name,emailAddress!!,usertype)
                        mFirebaseDatabase.child(userId!!).setValue(myUser).addOnCompleteListener(this,
                            OnCompleteListener { task ->
                                if (task.isSuccessful){
                                    val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
                                    var editor = sharedPreferences.edit()
                                    editor.putString("type",usertype)
                                    editor.commit()
                                    Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this,MainActivity::class.java))
                                    finish()
                                }else {
                                    Toast.makeText(this, "Data save Failed", Toast.LENGTH_LONG).show()
                                }
                            })
                    }else{
                        startActivity(Intent(this,com.example.petstate.errorpages.error::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                }
            })
    }

}

