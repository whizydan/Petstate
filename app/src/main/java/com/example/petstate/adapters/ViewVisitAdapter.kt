package com.example.petstate.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.petstate.R
import kotlinx.android.synthetic.main.activity_view_visit.view.*

class ViewVisitAdapter(private val context: Activity,
                       //private val petName: Array<String>, private val breed: Array<String>,
                       private val date: Array<String>, private val temp: Array<String>,
                       private val weight: Array<String> , private val prescription: Array<String>,
                       private val diagnosis: Array<String>

                       ) : ArrayAdapter<String>(context, R.layout.activity_view_visit, temp)



    {
        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val inflater = context.layoutInflater
            val rowView = inflater.inflate(R.layout.activity_view_visit, null, true)

            // val idText = rowView.findViewById(R.id.textViewId) as TextView
            val date = rowView.findViewById(R.id.tvDate) as TextView
            val temp = rowView.findViewById(R.id.tvTemp) as TextView
            val weight = rowView.findViewById(R.id.tvWeight) as TextView
            val prescription = rowView.findViewById(R.id.tvPrescription) as TextView
            val diagnosis = rowView.findViewById(R.id.tvDiagnosis) as TextView



            //idText.text = "Id: ${id[position]}"
            date.text = "date: ${date[position]}"
            temp.text = "temp: ${temp[position]}"
            weight.text = "weight: ${weight[position]}"
            prescription.text = "prescription: ${prescription[position]}"
            diagnosis.text = "diagnosis: ${diagnosis[position]}"


            return rowView
        }
    }

private operator fun TextView.get(position: Int): TextView {
return tvDate
}

