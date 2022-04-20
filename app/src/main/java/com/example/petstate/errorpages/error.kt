package com.example.petstate.errorpages

import android.app.ActivityManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.R


class error : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        val reset = findViewById<Button>(R.id.reset)

        reset.setOnClickListener {
            Runtime.getRuntime().exec("pm clear com.example.petstate")
        }
    }
}