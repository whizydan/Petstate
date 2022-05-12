package com.example.petstate.adapters

import android.content.Intent
import android.net.Uri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.petstate.MainActivity
import com.example.petstate.R

class Bottomsheet : BottomSheetDialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_page, container, false)
    }
    private fun link(url:String){
            val openurl= Intent(Intent.ACTION_VIEW)
            openurl.data= Uri.parse(url)
            startActivity(openurl)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val project = view.findViewById<ImageView>(R.id.project)
        val iano = view.findViewById<ImageView>(R.id.iano)
        val anto = view.findViewById<ImageView>(R.id.anto)
        val mj = view.findViewById<ImageView>(R.id.mj)
        val hilda = view.findViewById<ImageView>(R.id.hilda)
        val alvin = view.findViewById<ImageView>(R.id.alvo)
        val kerberos = view.findViewById<ImageView>(R.id.kerberos)

        project?.setOnClickListener {
            link("https://github.com/whizydan/petstate")
        }

        iano?.setOnClickListener {
            link("https://github.com/Ndathi")
        }
        alvin?.setOnClickListener {
            //TODO - fix link
            link("https://github.com/ianndathi")
        }
        mj?.setOnClickListener {
            link("https://github.com/joyMaryK")
        }
        kerberos?.setOnClickListener {
            link("https://github.com/whizydan")
        }
        anto?.setOnClickListener {
            link("https://github.com/tonnypac")
        }
        hilda?.setOnClickListener {
            //TODO - fix link
            link("https://github.com/ianndathi")
        }
    }
}