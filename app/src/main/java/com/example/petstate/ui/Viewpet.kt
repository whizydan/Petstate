package com.example.petstate.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.Add_Pet
import com.example.petstate.R
import com.example.petstate.adapters.ClientListAdapter
import com.example.petstate.databasetools.AddPetDataClass
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.messenger.TinyDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_viewpet.*
import kotlinx.android.synthetic.main.activity_welcome.*
import java.io.File
import java.net.URI
import java.nio.file.FileSystem
import java.nio.file.Files
import kotlin.io.path.Path

class Viewpet : AppCompatActivity() {
    private var tinydb: TinyDB? = null
    private var clientname :String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpet)
        val background = findViewById<ImageView>(R.id.visitsview)
        val lv = findViewById<ListView>(R.id.listings)
        val client_id = intent.getStringExtra("client_id")
        var i = client_id?.toInt()?.plus(1)
        val tvOwnerName = findViewById<TextView>(R.id.petName)
        val tvOwnerEmail = findViewById<TextView>(R.id.petType)
        val tvOwnerNumber = findViewById<TextView>(R.id.petBreed)
        val bg = findViewById<FloatingActionButton>(R.id.floatingActionButton4)
        tinydb = TinyDB(applicationContext)


        val id = i.toString()
        //  val id = client_id.toString()
        val db = DatabaseHandler(this)
        val data = db.readClientForViewPet(id)
        var j = 0
        tvOwnerName.append(data[j].clientName)
        tvOwnerEmail.append(data[j].clientEmail)
        tvOwnerNumber.append(data[j].phoneNumber)
        clientname = tvOwnerName.text.toString()
        val path = getExternalFilesDir(null).toString() + "/images/$clientname"

        val bitmap = tinydb!!.getImage(path)


        bg.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setBackground(resources.getDrawable(R.drawable.border))
                .setTitle("Update")
                .setMessage("Update picture")
                .setPositiveButton("Update") { dialog, _ ->
                    UpdatePic()
                    dialog.dismiss()
                }
                .setNegativeButton("cancel") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
        //var i = client_id
        // public val clientTest = i
        //set a random colour for the background
        var themeColor = getResources().getColor(R.color.themeColor)
        if (bitmap != null) {
            background?.setImageBitmap(bitmap)
        } else {
            //set status bar color to that of random color
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = themeColor
            background?.setBackgroundColor(themeColor)
        }


        /*query data from database -->
        using acquired clientID
        set hashmap set doubles using visit id's and visit dates and visit reason
        */
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val client: List<AddPetDataClass> = databaseHandler.readPet(id)
        //val empArrayId = Array<String>(emp.size) { "0" }
        val clientArrayName = Array<String>(client.size) { "null" }
        val clientArrayAddress = Array<String>(client.size) { "null" }
        val clientArrayEmail = Array<String>(client.size) { "null" }
        val clientArrayPhoneNo = Array<String>(client.size) { "null" }
        val petColor = Array<String>(client.size) { "null" }
        var index = 0
        client.forEach { c ->
            // empArrayId[index] = e.userId.toString()
            clientArrayName[index] = c.petName
            clientArrayAddress[index] = c.petDOB
            clientArrayEmail[index] = c.petType
            clientArrayPhoneNo[index] = c.petBreed
            petColor[index] = c.petColour
            index++
        }
        //creating custom ArrayAdapter
        val thisListAdapter = ClientListAdapter(
            this,

            clientArrayName,
            clientArrayAddress,
        )

        lv.adapter = thisListAdapter
        lv.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemIdAtPosition(position)
            val intent = Intent(this, Viewpetdetails::class.java)
            intent.putExtra("pet_id", element.toString())
            startActivity(intent)

        }

        addVisit.setOnClickListener {
            val intent = Intent(this, Add_Pet::class.java)
            val element = client_id
            intent.putExtra("i", element.toString())
            startActivity(intent)
        }


    }

    private fun UpdatePic() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val tinyDB = TinyDB(applicationContext)
        val view = findViewById<ImageView>(R.id.visitsview)
        if (resultCode == Activity.RESULT_OK && requestCode == 100)
        {
            view.setImageURI(data?.data)
            val newbitmap = (view.drawable as BitmapDrawable).bitmap
            tinyDB.putImage(
                "images",
                clientname,
                newbitmap
            )
        }
    }

}