package com.example.petstate.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.petstate.MainActivity
import com.example.petstate.R
import com.example.petstate.databasetools.clientsDataClass
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.messenger.TinyDB

class NewClientDetails : AppCompatActivity() {
    private var picture : ImageView? = null
    private var bitmap: Bitmap? = null
    private var file_uri :Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client_details)

        val edClientName = findViewById<EditText>(R.id.edClientName)
        picture = findViewById(R.id.ivProfilePic)
        //val edPetName = findViewById<EditText>(R.id.edPetName)
        val edPhoneNo = findViewById<EditText>(R.id.edPhoneNo)
        val edClientsEmail = findViewById<EditText>(R.id.edClientsEmail)
        val edClientPostalAddress = findViewById<EditText>(R.id.edClientPostalAddress)
       // val btCancel = findViewById<Button>(R.id.btCancel)
        val btSave = findViewById<Button>(R.id.btSave)


        picture?.setOnClickListener {
            openGalleryForImage()
        }


       //               save button to enter values to db

        btSave.setOnClickListener {
            val tinyDB = TinyDB(applicationContext)
            val name = edClientName.getText().toString()

                val phoneNo = edPhoneNo.getText().toString()
            val address = edClientPostalAddress.getText().toString()
            val email = edClientsEmail.getText().toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            //                       validation
            if (name.trim()!="" && email.trim()!=""
                && phoneNo.trim()!="" && address.trim()!=""
            ) {
                val status = databaseHandler.createClient(
                    clientsDataClass(
                        clientName = name,// client_id = client_id,
                        clientEmail = email, clientAddress = address,
                    phoneNumber =phoneNo)
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "details saved", Toast.LENGTH_LONG).show()
                    edClientName.text.clear()
                    edClientPostalAddress.text.clear()
                    edClientsEmail.text.clear()
                    //edPetName.text.clear()
                    edPhoneNo.text.clear()
                    tinyDB.putImage(
                        "images",
                        name,
                        bitmap
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        applicationContext,
                        "fields cannot be empty",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }

            finish()
        }


    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100)
        {
            picture?.setImageURI(data?.data)
            bitmap =(picture?.drawable as BitmapDrawable).bitmap
        }
    }
}


