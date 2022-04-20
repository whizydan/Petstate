package com.example.petstate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils
import kotlinx.android.synthetic.main.activity_vet_signup.*

class vetSignup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vet_signup)
        auth = FirebaseAuth.getInstance()


        val email = findViewById<EditText>(R.id.Email2)
        val password = findViewById<EditText>(R.id.Password2)
        val btn = findViewById<Button>(R.id.buttonRegister)
        val name = findViewById<EditText>(R.id.PersonName)
        val add = findViewById<EditText>(R.id.Address)
        val institutionName = findViewById<EditText>(R.id.instName)


        buttonRegister.setOnClickListener {

            when {

                TextUtils.isEmpty(email.text.toString().replace(" ".toRegex(), "")) -> {
                    Toast.makeText(
                        this, "please enter email", Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty(add.text.toString().replace(" ".toRegex(), "")) -> (
                    Toast.makeText(
                        this, "please enter an address", Toast.LENGTH_SHORT
                    ).show()
                        )

                TextUtils.isEmpty(phone.text.toString().replace(" ".toRegex(), "")) -> (
                        Toast.makeText(
                            this, "please enter an phone", Toast.LENGTH_SHORT
                        ).show()
                        )
            }




    }
    }
}
