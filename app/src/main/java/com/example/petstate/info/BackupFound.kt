package com.example.petstate.info

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.example.petstate.MainActivity
import com.example.petstate.R
import com.example.petstate.petowner.RecordListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_backup_found.*
import java.io.File

class BackupFound : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup_found)
        val mAuth = FirebaseAuth.getInstance()
        val type = intent.getStringExtra("type")
        var uid = mAuth.uid.toString()
        val restore = findViewById<Button>(R.id.restoredb)

        restore.setOnClickListener {
            download(uid,type.toString())
        }



        floatingActionButton2.setOnClickListener {
            if (type == "pet owner"){
                startActivity(Intent(this,RecordListActivity::class.java))
                finish()
            }else if (type == "vet"){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else {
                startActivity(Intent(this,com.example.petstate.errorpages.error::class.java))
                finish()
            }
        }


    }



    private fun download(uid:String, type:String){
        val storageRef = FirebaseStorage.getInstance()
        val islandRef = storageRef.getReference().child("backups/$uid")
        val localFile = File.createTempFile("$uid",".sqlite")
        var filename = localFile.name
        islandRef.getFile(localFile).addOnSuccessListener {
            Toast.makeText(this,"success",Toast.LENGTH_SHORT).show()
            //rename file to RECORDDB.SQLITE if petowner and other if vet
            if (type == "vet"){
                    val src = File("/data/data/com.example.petstate/cache/$filename")
                    val dest = File("/data/data/com.example.petstate/databases/EmployeeDatabase")
                try {
                    src.copyTo(dest,true)
                }catch (e :Exception){
                    backup.setText(e.toString())
                }

            }
            else if (type == "pet owner"){
                val src =  File("/data/data/com.example.petstate/cache/$filename")
                val dest = File("/data/data/com.example.petstate/databases/RECORDDB.sqlite")
                    try {
                        src.copyTo(dest,true)
                    }catch (e :Exception){
                        backup.setText(e.toString())
                    }
            }
            else{
                Toast.makeText(this,"user data invalid",Toast.LENGTH_SHORT).show()
            }
            val animAte = findViewById<LottieAnimationView>(R.id.lottieAnimationView3)
            animAte.setAnimation(R.raw.success)
            animAte.playAnimation()
            backup.setText("your Data has been restored,please continue")
            restoredb.isVisible = false
        }.addOnFailureListener{
            val animAte = findViewById<LottieAnimationView>(R.id.lottieAnimationView3)
            animAte.setAnimation(R.raw.empty)
            animAte.playAnimation()
            val backup = findViewById<TextView>(R.id.backup)
            backup.setText("Backup not available")
            restoredb.setText("retry")
            Toast.makeText(this,"No backups found",Toast.LENGTH_SHORT).show()
        }.addOnProgressListener {
            val animAte = findViewById<LottieAnimationView>(R.id.lottieAnimationView3)
            animAte.setAnimation(R.raw.downloading)
            animAte.playAnimation()
            backup.setText("please wait while we download the file for you")
            restoredb.isVisible = false
        }
    }
}