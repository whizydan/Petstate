package com.example.petstate.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.petstate.MainActivity
import com.example.petstate.R
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.databasetools.ReportDataClass
import com.example.petstate.databasetools.clientsDataClass
import kotlinx.android.synthetic.main.activity_sick_pet_report.*

class SickPetReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_pet_report)

        saveButton.setOnClickListener {
            //val id = u_id.text.toString()
            val fur = etFur.getText().toString()
            val sleeping = etSleeping.getText().toString()
            val feeding = etFeeding.getText().toString()
            val other = etSymptoms.getText().toString()
            val bowel = etMuzzle.getText().toString()
            val behaviour = etEyeColor.getText().toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            //                       validation
            if (fur.trim()!="" && sleeping.trim()!=""
                && feeding.trim()!="" && other.trim()!="" && bowel.trim()!="" && behaviour.trim()!=""
            ) {
                val status = databaseHandler.createReport(
                    ReportDataClass(
                        feeding = feeding,
                        bowel = bowel,
                       fur = fur,
                        other = other,
                    behaviour=behaviour,
                        sleeping=sleeping
                    )
                )
                if (status > -1) {
                    //Toast.makeText(applicationContext, "details saved", Toast.LENGTH_LONG).show()
                    etEyeColor.text.clear()
                    etFeeding.text.clear()
                    etFur.text.clear()
                    etMuzzle.text.clear()
                    etSleeping.text.clear()
                    etSymptoms.text.clear()



                   // val intent = Intent(this, MainActivity::class.java)
                   // startActivity(intent)

                }
                Toast.makeText(applicationContext, "details saved", Toast.LENGTH_LONG).show()
            }
                else {
                    Toast.makeText(
                        applicationContext,
                        "fields cannot be empty",
                        Toast.LENGTH_LONG
                    ).show()
                }




            finish()

    }
 }
}