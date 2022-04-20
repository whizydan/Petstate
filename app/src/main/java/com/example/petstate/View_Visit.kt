package com.example.petstate

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.databasetools.AddPetDataClass
import com.example.petstate.databasetools.DatabaseHandler


class View_Visit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_visit)

        val visit_id = intent.getStringExtra("visit_id")
        val toast=Toast.makeText(applicationContext,visit_id, Toast.LENGTH_SHORT)
        toast.show()

        var i= visit_id?.toInt()?.plus(1)
        val id=i.toString()

        val tvDate: TextView = findViewById(com.example.petstate.R.id.tvDate)
        val tvTemp: TextView = findViewById(com.example.petstate.R.id.tvTemp)
        val tvWeight: TextView = findViewById(com.example.petstate.R.id.tvWeight)
        val tvPrescription: TextView = findViewById(com.example.petstate.R.id.tvPrescription)
        val tvDiagnosis: TextView = findViewById(com.example.petstate.R.id.tvDiagnosis)
        val db  : DatabaseHandler = DatabaseHandler(this)
        val data = db.viewVisitRead(id)
        var j= 0
        tvDate.text = ""



            tvDate.append(data[j!!].date.toString())
            tvTemp.append(data[j].temperature.toString())
            tvWeight.append(data[j].weight.toString())
            tvPrescription.append(data[j].prescription.toString())
            tvDiagnosis.append(data[j].diagnosis.toString())


    }


        }

