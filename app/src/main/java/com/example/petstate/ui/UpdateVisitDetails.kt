package com.example.petstate.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.R
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.databasetools.UpdateVisitDataClass

class UpdateVisitDetails: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_visit_details)

        val updateDiagnosis = findViewById<EditText>(R.id.updateDiagnosis)
        val updatePrescriptionDetails = findViewById<EditText>(R.id.updatePrescriptionDetails)
        val updateTemp = findViewById<EditText>(R.id.updateTemp)
        val updateWeight = findViewById<EditText>(R.id.updateWeight)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)

        buttonSave.setOnClickListener {
            val upDiagnosis = updateDiagnosis.getText().toString()
            val upPrescription = updatePrescriptionDetails.getText().toString()
            val upTemp = updateTemp.getText().toString()
            val upWeight = updateWeight.getText().toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)


            if (upDiagnosis.trim() != "" && upTemp.trim() != ""
                && upWeight.trim() != "" && upPrescription.trim() != ""
            ) {
                val status = databaseHandler.updateVisit(
                    UpdateVisitDataClass(
                        updateDiagnosis = upDiagnosis,
                        updatePrescription = upPrescription, updateTemperature = upTemp,
                        updateWeight = upWeight
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "details saved", Toast.LENGTH_LONG).show()
                    updateDiagnosis.text.clear()
                    updatePrescriptionDetails.text.clear()
                    updateTemp.text.clear()
                    //edPetName.text.clear()
                    updateWeight.text.clear()


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