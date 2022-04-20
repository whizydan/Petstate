package com.example.petstate.ui

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.R
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.databasetools.VetListDataClass

class VetList : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vet_signup)

        val Email = findViewById<EditText>(R.id.Email2)
        val Phone = findViewById<EditText>(R.id.phone)
        val Hospital = findViewById<EditText>(R.id.instName)
        val Address = findViewById<EditText>(R.id.Address)
        val Name = findViewById<EditText>(R.id.PersonName)
        val btnReg = findViewById<Button>(R.id.buttonRegister)




        btnReg.setOnClickListener{
            val email = Email.getText().toString()
            val phone = Phone.getText().toString()
            val hospital = Hospital.getText().toString()
            val name = Name.getText().toString()
            val address = Address.getText().toString()




            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            //                       validation
            if (email.trim()!="" && phone.trim()!="" && hospital.trim()!="" &&
                name.trim()!="" && address.trim()!=""
            ) {
                val status = databaseHandler.createVetList(
                    VetListDataClass(
                        vetEmail = email,
                        vetPhone = phone,
                        vetHospital = hospital, vetAddress = address, vetName = name
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "details saved", Toast.LENGTH_LONG).show()
                    Email.text.clear()
                    Phone.text.clear()
                    Hospital.text.clear()
                    Address.text.clear()
                    Name.text.clear()



                } else {
                    Toast.makeText(
                        applicationContext,
                        "fields cannot be empty",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }


        }



    }


}