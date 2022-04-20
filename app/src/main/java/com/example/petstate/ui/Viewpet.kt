package com.example.petstate.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.Add_Pet
import com.example.petstate.R
import com.example.petstate.adapters.ClientListAdapter
import com.example.petstate.databasetools.AddPetDataClass
import com.example.petstate.databasetools.DatabaseHandler
import kotlinx.android.synthetic.main.activity_viewpet.*
import kotlinx.android.synthetic.main.activity_welcome.*

class Viewpet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpet)
        val background = findViewById<View>(R.id.visitsview)
        val lv = findViewById<ListView>(R.id.listings)
        val client_id = intent.getStringExtra("client_id")
        var i= client_id?.toInt()?.plus(1)
        val tvOwnerName =findViewById<TextView>(R.id.petName)
        val tvOwnerEmail =findViewById<TextView>(R.id.petType)
        val tvOwnerNumber =findViewById<TextView>(R.id.petBreed)



        val id =i.toString()
      //  val id = client_id.toString()
        val db  : DatabaseHandler = DatabaseHandler(this)
        val data = db.readClientForViewPet(id)
        var j= 0
        tvOwnerName.append(data[j!!].clientName.toString())
        tvOwnerEmail.append(data[j].clientEmail.toString())
        tvOwnerNumber.append(data[j].phoneNumber.toString())


        val toast=Toast.makeText(applicationContext,id, Toast.LENGTH_SHORT)
        toast.show()
        //var i = client_id
       // public val clientTest = i
        //set a random colour for the background
        var themeColor = getResources().getColor(R.color.themeColor)
        //set status bar color to that of random color
        if (Build.VERSION.SDK_INT >= 21){
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = themeColor
        }
        background.setBackgroundColor(themeColor)

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
        val clientArrayEmail =Array<String>(client.size){"null"}
        val clientArrayPhoneNo =Array<String>(client.size){"null"}
        val petColor =Array<String>(client.size){"null"}
        var index = 0
        client.forEach { c ->
            // empArrayId[index] = e.userId.toString()
            clientArrayName[index] = c.petName
            clientArrayAddress[index] = c.petDOB
            clientArrayEmail[index] =c.petType
            clientArrayPhoneNo[index]=c.petBreed
            petColor[index]=c.petColour
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
            val intent = Intent(this,Viewpetdetails::class.java)
           intent.putExtra("pet_id",element.toString())
            startActivity(intent)

        }

        addVisit.setOnClickListener {
            val intent = Intent(this,Add_Pet::class.java)
            val element = client_id
            intent.putExtra("i",element.toString())
            startActivity(intent)
        }


    }

}