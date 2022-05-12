@file:Suppress("UsePropertyAccessSyntax")

package com.example.petstate.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.MainActivity
import com.example.petstate.R
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.databasetools.NewVisitDataClass
import java.time.LocalDateTime

class NewVisitDetails : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_visit_details)

        val inputDiagnosis = findViewById<EditText>(R.id.inputDiagnosis)
        val inputTemp = findViewById<EditText>(R.id.inputTemp)
        val inputWeight = findViewById<EditText>(R.id.inputWeight)
        val inputPrescriptionDetails = findViewById<EditText>(R.id.inputPrescriptionDetails)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)
        val dateNow = findViewById<TextView>(R.id.dateNow)
        val pet_id = intent.getStringExtra("i").toString()
        val toast=Toast.makeText(applicationContext,pet_id, Toast.LENGTH_SHORT)
        toast.show()
        var datetNow = LocalDateTime.now()
        dateNow.append(datetNow.toString())
        buttonSave.setOnClickListener{
            val diagnosis = inputDiagnosis.getText().toString()
            val temp = inputTemp.getText().toString()
            val weight = inputWeight.getText().toString()
            val prescription = inputPrescriptionDetails.getText().toString()
            //val dateNow = LocalDateTime.now()
            val dateNoww = dateNow.getText().toString()
            var i= pet_id?.toInt()?.plus(1)



            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            //                       validation
            if (diagnosis.trim()!="" && temp.trim()!=""
                && weight.trim()!="" && prescription.trim()!="" && dateNoww.trim()!=""
            ) {
                val status = databaseHandler.createVisit(
                    NewVisitDataClass(
                        vetDiagnosis = diagnosis,
                        vetPrescription = prescription, petTemperature = temp,
                        petWeight = weight, dateNow = dateNoww, pet_id = pet_id
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "details saved", Toast.LENGTH_LONG).show()
                    inputDiagnosis.text.clear()
                    inputTemp.text.clear()
                    inputWeight.text.clear()
                    //edPetName.text.clear()
                    inputPrescriptionDetails.text.clear()
                        finish()
                  //  val intent = Intent(this, Viewpetdetails::class.java)
                  //  startActivity(intent)

                } else {
                    Toast.makeText(
                        applicationContext,
                        "fields cannot be empty",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
            //finish()

        }



    }


}
