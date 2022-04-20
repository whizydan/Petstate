package com.example.petstate.ui

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.R
import com.example.petstate.adapters.VetListAdapter
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.databasetools.VetListDataClass

class VetListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vet_list)
        //val background = findViewById<View>(R.id.visitsview)
        val lv = findViewById<ListView>(R.id.listViewVet)


        //set a random colour for the background
        var themeColor = getResources().getColor(R.color.themeColor)
        //set status bar color to that of random color
        if (Build.VERSION.SDK_INT >= 21){
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = themeColor
        }
       // background.setBackgroundColor(themeColor)

        /*query data from database -->
        using acquired clientID
        set hashmap set doubles using visit id's and visit dates and visit reason
        */
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val vet: List<VetListDataClass> = databaseHandler.readVets()
        //val empArrayId = Array<String>(emp.size) { "0" }
        val vetArrayName = Array<String>(vet.size) { "null" }
        val vetArrayAddress = Array<String>(vet.size) { "null" }
        val vetArrayEmail =Array<String>(vet.size){"null"}
        val vetArrayPhone =Array<String>(vet.size){"null"}
        val vetArrayHospital =Array<String>(vet.size){"null"}
        var index = 0
        vet.forEach { v ->
            // empArrayId[index] = e.userId.toString()
            vetArrayName[index] = v.vetName
            vetArrayAddress[index] = v.vetAddress
            vetArrayEmail[index] =v.vetEmail
            vetArrayPhone[index]=v.vetPhone
            vetArrayHospital[index]=v.vetHospital
            index++
        }
        //creating custom ArrayAdapter
        val thisListAdapter = VetListAdapter(
            this,

            vetArrayName,
            vetArrayAddress,
            vetArrayEmail,
            vetArrayHospital,
            vetArrayPhone
        )

        lv.adapter = thisListAdapter
//        lv.setOnItemClickListener { parent, view, position, id ->
//            val intent = Intent(this,Viewpetdetails::class.java)
//            startActivity(intent)
//        }

//        addVisit.setOnClickListener {
//            val intent = Intent(this,Add_Pet::class.java)
//            startActivity(intent)
//        }


    }

}