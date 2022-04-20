package com.example.petstate.tutorial

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.petstate.R
import com.example.petstate.security.Login
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_about_pets.*

class about_pets : AppCompatActivity() {
    companion object{
        private const val STORAGE_PERMISSION_CODE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_pets)
        val next = findViewById<FloatingActionButton>(R.id.next)
        val storage = findViewById<Button>(R.id.permission)

        next.setOnClickListener {
            saveprefs()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left)
            finish()
        }


        storage.setOnClickListener {
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        }
    }

    private fun checkPermission(permission:String,requestcode:Int){
        if (ContextCompat.checkSelfPermission(this@about_pets,permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this@about_pets, arrayOf(permission),requestcode)
        }else
        {
            Toast.makeText(this@about_pets,"permission already granted",Toast.LENGTH_SHORT).show()
            next.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this@about_pets,"permission granted",Toast.LENGTH_SHORT).show()
                next.show()
                //permission granted
            }
            else {
                Toast.makeText(this@about_pets,"permission denied",Toast.LENGTH_SHORT).show()
                //permission denied
            }
        }
    }

    private fun saveprefs(){
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putString("firsttimeinstall","false")
        editor.commit()
    }
}