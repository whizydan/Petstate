package com.example.petstate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.databasetools.Test
import com.example.petstate.ui.Viewpet

class Add_Pet : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
   // private lateinit var binding: ActivityAddPet2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet2)

        //binding = ActivityAddPet2Binding.inflate(layoutInflater)
     //   setContentView(binding.root)

       // setSupportActionBar(binding.toolbar)

        //val navController = findNavController(R.id.nav_host_fragment_content_add_pet2)
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)

        val btSaveNewPet = findViewById<Button>(R.id.btSaveNewPet)
        val edPetName = findViewById<EditText>(R.id.ed_pet_name)
        val edPetDOB = findViewById<EditText>(R.id.ed_pet_dob)
        val edpetType = findViewById<EditText>(R.id.edPetType)
        val edpetBreed = findViewById<EditText>(R.id.edPetBreed)
        val edPetColour = findViewById<EditText>(R.id.edPetColour)

        val client_id = intent.getStringExtra("i")
        val toast=Toast.makeText(applicationContext,client_id, Toast.LENGTH_SHORT)
        toast.show()



        btSaveNewPet.setOnClickListener {
            //val id = u_id.text.toString()
            val petName = edPetName.getText().toString()
            val petColour = edPetColour.getText().toString()
            val petDOB = edPetDOB.getText().toString()
            val petType = edpetType.getText().toString()
            val petBreed = edpetBreed.getText().toString()
            var i= client_id?.toInt()?.plus(1)
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            //                       validation
            if (petName.trim() != "" && petColour.trim() != ""
                && petDOB.trim() != "" && petType.trim() != "" && petBreed.trim() != ""
            ) {
                val status = databaseHandler.createPet(
                    Test(
                        petName = petName,
                        petColour = petColour, petDOB = petDOB,
                        petType = petType, petBreed = petBreed, client_id = i
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "details saved", Toast.LENGTH_LONG).show()
                    edPetName.text.clear()
                    edpetType.text.clear()
                    edPetColour.text.clear()
                    edPetDOB.text.clear()
                    edpetBreed.text.clear()
                        finish()
                    //val intent = Intent(this, Viewpet::class.java)
                   // startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "fields cannot be empty",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
           // finish()
        }

//        override fun onSupportNavigateUp(): Boolean {
//            val navController = findNavController(R.id.nav_host_fragment_content_add_pet2)
//            return navController.navigateUp(appBarConfiguration)
//                    || super.onSupportNavigateUp()
//        }
    }
}