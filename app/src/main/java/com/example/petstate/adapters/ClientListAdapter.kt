package com.example.petstate.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.petstate.R
import com.google.android.material.card.MaterialCardView

class ClientListAdapter(private val context: Activity,
                         private val name: Array<String>, private val address: Array<String>)
    : ArrayAdapter<String>(context, R.layout.client_list, name)

{
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.client_list, null, true)


       // val idText = rowView.findViewById(R.id.textViewId) as TextView
        val clientNameText = rowView.findViewById(R.id.tvClientName) as TextView
        val addressText = rowView.findViewById(R.id.tvAddress) as TextView

        //idText.text = "Id: ${id[position]}"
        clientNameText.text = " ${name[position]}"
        addressText.text = "${address[position]}"
        return rowView
    } 
}

