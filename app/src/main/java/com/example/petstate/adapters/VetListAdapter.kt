package com.example.petstate.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.petstate.R
import com.google.firebase.auth.FirebaseAuth

class VetListAdapter(private val context: Activity,
                        private val name: Array<String>, private val address: Array<String>,
                     private val email: Array<String>, private val hospital: Array<String>,
                     private val phone: Array<String>)
    : ArrayAdapter<String>(context, R.layout.vet_list, name)

{
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.vet_list, null, true)

        // val idText = rowView.findViewById(R.id.textViewId) as TextView
        val Name = rowView.findViewById(R.id.vetName) as TextView
        val Email = rowView.findViewById(R.id.vetEmail) as TextView
        val Hospital = rowView.findViewById(R.id.vetHospital) as TextView
        val Address = rowView.findViewById(R.id.vetAddress) as TextView
        val Phone = rowView.findViewById(R.id.vetPhone) as TextView

        //idText.text = "Id: ${id[position]}"
        Name.text = " ${name[position]}"
        Address.text = "${address[position]}"
        Email.text = "${email[position]}"
        Hospital.text = "${hospital[position]}"
        Phone.text = "${phone[position]}"
        return rowView
    }

}
