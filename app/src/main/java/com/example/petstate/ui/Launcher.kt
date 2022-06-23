package com.example.petstate.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.security.Login
import java.util.*
import android.content.Intent
import com.example.petstate.MainActivity
import com.example.petstate.messenger.TinyDB
import com.google.firebase.auth.FirebaseAuth
import com.example.petstate.petowner.Main
import com.example.petstate.petowner.RecordListActivity
import com.example.petstate.tutorial.about_pets
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlin.concurrent.schedule

class Launcher : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*no need to setContentView(R.layout.launcher) since that will take time to process the layout
         instead use window background in themes.xml

         when the device is relatively fast it does not display this page for an adequate time
         hence the need for a timer.*/
        Timer("launch",false).schedule(1000){
            launch()
        }

    }
    fun runner(){
        val tinydb = TinyDB(applicationContext)
        val key = tinydb.getString("type")
        if (FirebaseAuth.getInstance().currentUser?.uid  == null){
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }else if(key == "pet owner"){
            val intent = Intent(this,RecordListActivity::class.java)
            startActivity(intent)
            finish()
        }else if (key == "vet"){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else if (key == "null"){
            val intent = Intent(this,com.example.petstate.errorpages.error::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun launch() {
        //check if user is logged in or not
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val key = sharedPreferences.getString("firsttimeinstall","true")
        //if not first time install go to home
        if (key == "false"){
            runner()
        }else if(key == "true"){
            val intent = Intent(this,about_pets::class.java)
            startActivity(intent)
            finish()
        }

    }

}