package com.example.petstate.security

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.petstate.petowner.Main
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.utils.Oscillator
import com.example.petstate.MainActivity
import com.example.petstate.R
import com.example.petstate.info.BackupFound
import com.example.petstate.messenger.TinyDB
import com.example.petstate.petowner.RecordListActivity
import com.example.petstate.petowner.post.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_welcomepost.*


class Login() : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var userId: String? = null
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mFirebaseInstance = FirebaseDatabase.getInstance()
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val email = findViewById<TextInputEditText>(R.id.inputemail)
        val password = findViewById<TextInputEditText>(R.id.inputpassword)
        val forgotpassword = findViewById<TextView>(R.id.fogtPwd)
        val register = findViewById<TextView>(R.id.register)
        val login = findViewById<Button>(R.id.btnLogin)
        val textField = findViewById<TextInputLayout>(R.id.menus)
        val items = listOf("vet","pet owner")
        val adapter = ArrayAdapter(applicationContext,R.layout.userlisttype,items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        register.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
            finish()
        }

        forgotpassword.setOnClickListener {
            startActivity(Intent(this@Login, ForgotPasswordActivity::class.java))
            finish()
        }

        login.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            when {
                TextUtils.isEmpty(email.text.toString().replace(" ".toRegex(), "")) -> {
                    Toast.makeText(
                        this@Login, "please enter email", Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty(password.text.toString().replace(" ".toRegex(), "")) -> {
                    Toast.makeText(
                        this@Login, "please enter a password", Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty((textField.editText as? AutoCompleteTextView)?.editableText.toString().replace(" ".toRegex(), "")) -> {
                    textField.setError("please fill user type")
                    Toast.makeText(
                        this@Login, "please select user type", Toast.LENGTH_SHORT
                    ).show()

                }

                else -> {
                    val email = email.getText().toString().trim()
                    val password = password.getText().toString().trim()
                    val usermodel = (textField.editText as? AutoCompleteTextView)?.editableText.toString()

                    /*mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                //Display newly updated nme and email
                                val usermodel = (textField.editText as? AutoCompleteTextView)?.editableText.toString()
                                userId = mAuth.uid
                                addUserChangeListener(userId!!,usermodel)
                                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                            }
                        })*/
                    mAuth.signInAnonymously()
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG)
                                    .show()
                                launch(usermodel)
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Logg In failed", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                }
            }
        }

    }

    private fun launch(type:String) {
        val intent = Intent(this, BackupFound::class.java)
        intent.putExtra("type",type)
        val tiny = TinyDB(applicationContext)
        tiny.putString("type",type)
        startActivity(intent)
        finish()
    }

    private fun addUserChangeListener(userId:String,usermodel:String){
        //User data change Listener
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users/$usermodel")
        mFirebaseDatabase!!.child(userId).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val user=dataSnapshot.getValue(User::class.java)
                //Check for null
                if(user==null){
                    Toast.makeText(this@Login,"user data is null",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Login,com.example.petstate.errorpages.error::class.java))
                    return
                }
                Log.e(Oscillator.TAG,"User data is changed!"+user.name+","+user.email + " "+ user.type)

                //Display newly updated name and email

                val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
                var editor = sharedPreferences.edit()
                editor.putString("type", user.type)
                editor.putString("email", user.email)
                editor.putString("name", user.name)
                editor.commit()
                launch(user.type)


            }

            override fun onCancelled(error: DatabaseError){
                //Failed to read value
                Log.e(Oscillator.TAG,"Failed to read user",error.toException())
                launcherror()
            }
        })
    }

    fun launcherror(){
        val intent = Intent(this,com.example.petstate.errorpages.error::class.java)
        startActivity(intent)
        finish()
    }
}

