package com.example.petstate.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.petstate.MainActivity
import com.example.petstate.PetCareInfoActivity
import com.example.petstate.R
import com.example.petstate.security.Login
import com.example.petstate.security.Signup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_owner_vs_vet.*

class owner_vs_vet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_vs_vet)

        val btOwner: Button = findViewById(R.id.btOwner)
        btOwner.setOnClickListener {
        launch2()
        }

        val btVet : Button = findViewById(R.id.btVet)
        btVet.setOnClickListener {
        launch()
        }
    }
    private fun launch() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)

        }
    }
        private fun launch2() {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                startActivity(Intent(this, PetCareInfoActivity::class.java))
            }

        }

}
