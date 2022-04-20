package com.example.petstate.adapters


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.petstate.R

class UpdateVisitAdapter(private val context: Activity,
                         private val upDiagnosis: Array<String>, private val upPrescription: Array<String>,
                         private val upTemp: Array<String>, private val upWeight: Array<String>)
    : ArrayAdapter<String>(context, R.layout.update_visit_details, upDiagnosis)

{
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.update_visit_details, null, true)

        // val idText = rowView.findViewById(R.id.textViewId) as TextView
        val tempText = rowView.findViewById(R.id.updateTemp) as TextView
        val weightText = rowView.findViewById(R.id.updateWeight) as TextView
        val diagnosisText = rowView.findViewById(R.id.updateDiagnosis) as TextView
        val prescriptionText = rowView.findViewById(R.id.updatePrescriptionDetails) as TextView

        //idText.text = "Id: ${id[position]}"
        tempText.text = " ${upTemp[position]}"
        weightText.text = "${upWeight[position]}"
        diagnosisText.text = "${upDiagnosis[position]}"
        prescriptionText.text = "${upPrescription[position]}"
        return rowView
    }
}