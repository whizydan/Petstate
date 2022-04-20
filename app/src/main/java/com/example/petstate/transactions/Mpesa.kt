package com.example.petstate.transactions

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType
import com.example.petstate.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class Mpesa : AppCompatActivity() {

    lateinit var daraja: Daraja

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mpesa)
        val phonepay = findViewById<EditText>(R.id.phonepay)
        val buttonpay = findViewById<Button>(R.id.buttonpay)

    }
}