package com.example.petstate.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.R
import com.example.petstate.View_Visit
import com.example.petstate.adapters.ClientListAdapter
import com.example.petstate.databasetools.AddPetDataClass
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.databasetools.ViewVisitDataClass
import kotlinx.android.synthetic.main.activity_welcome.*

class Viewpetdetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpetdetails)
        val add_pet = findViewById<Button>(R.id.addVisit)
        val view2 = findViewById<View>(R.id.visitsview)
        val ls = findViewById<ListView>(R.id.visitslist)
        val tvpetName = findViewById<TextView>(R.id.petName)
        val tvpetType = findViewById<TextView>(R.id.petType)
        val tvpetBreed = findViewById<TextView>(R.id.petBreed)
        val client_id = intent.getStringExtra("pet_id")
        var i= client_id?.toInt()?.plus(1)
        val id=i.toString()
        val db  : DatabaseHandler = DatabaseHandler(this)
        val data = db.readPetForViewPetDetails(id)
        var j= 0
        tvpetName.append(data[j!!].petName.toString())
        tvpetType.append(data[j].petType.toString())
        tvpetBreed.append(data[j].petBreed.toString())

        add_pet.setOnClickListener {
            val intent = Intent(this,NewVisitDetails::class.java)
            val element = i
            intent.putExtra("i",element.toString())
            startActivity(intent)
            finish()
        }
        //set a random colour for the background
        var themeColor = getResources().getColor(R.color.colorAccent)
        //set status bar color to that of random color
        if (Build.VERSION.SDK_INT >= 21){
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = themeColor
        }
        view2.setBackgroundColor(themeColor)



        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val client: List<AddPetDataClass> = databaseHandler.readVisit(id)
        //val empArrayId = Array<String>(emp.size) { "0" }
        val clientArrayName = Array<String>(client.size) { "null" }
        val clientArrayAddress = Array<String>(client.size) { "null" }
        val clientArrayEmail =Array<String>(client.size){"null"}
        val clientArrayPhoneNo =Array<String>(client.size){"null"}
        val weight = Array<String>(client.size){"null"}
        var index = 0
        client.forEach { c ->
            // empArrayId[index] = e.userId.toString()
            clientArrayName[index] = c.petName
            clientArrayAddress[index] = c.petDOB
            clientArrayEmail[index] =c.petType
            clientArrayPhoneNo[index]=c.petBreed
            weight[index] = c.petColour
            index++
        }
        //creating custom ArrayAdapter
        val thisListAdapter = ClientListAdapter(
            this,

            clientArrayName,
            clientArrayAddress,
        )

        ls.adapter = thisListAdapter
        ls.setOnItemClickListener { parent, _, position, _ ->
            val element = parent.getItemIdAtPosition(position)
            val intent = Intent(this, View_Visit::class.java)
            intent.putExtra("visit_id",element.toString())
            startActivity(intent)
        }


    }
}