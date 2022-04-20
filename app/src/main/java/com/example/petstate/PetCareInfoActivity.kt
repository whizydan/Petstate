package com.example.petstate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.databinding.ActivityPetCareInfoBinding
import com.example.petstate.ui.NewClientDetails
import com.example.petstate.ui.SickPetReport
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class PetCareInfoActivity : AppCompatActivity() {

  //  private lateinit var binding: ActivityPetCareInfoBinding
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_care_info)

      /*  val fabReport: FloatingActionButton = findViewById(R.id.fabReport1)
        fabReport.setOnClickListener() {

            val intent = Intent(this, SickPetReport::class.java)
            startActivity(intent)

        }*/
       // binding = ActivityPetCareInfoBinding.inflate(layoutInflater)
        //setContentView(binding.root)

       // setSupportActionBar(findViewById(R.id.toolbar))
        //binding.toolbarLayout.title = title
        //binding.fab.setOnClickListener { view ->
          //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //    .setAction("Action", null).show()

        }
    }
