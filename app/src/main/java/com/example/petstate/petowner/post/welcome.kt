package com.example.petstate.petowner.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.utils.Oscillator.TAG
import com.example.petstate.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_welcomepost.*

class welcome1 : AppCompatActivity() {

    private var mFirebaseDatabase: DatabaseReference?=null
    private var mFirebaseInstance: FirebaseDatabase?=null

    var userId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcomepost)

        mFirebaseInstance= FirebaseDatabase.getInstance()

        //get reference to 'users' node
        mFirebaseDatabase=mFirebaseInstance!!.getReference("users")

        val user=FirebaseAuth.getInstance().currentUser

        //add it only if it is not saved to database
        if (user != null) {
            userId=user.uid
        }
        addUserChangeListener()
    }
    private fun addUserChangeListener(){
        
        //User data change Listener
        mFirebaseDatabase!!.child(userId!!).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val user=dataSnapshot.getValue(User::class.java)

                //Check for null
                if(user==null){
                    Log.e(TAG,"User data is null")
                    return
                }
                Log.e(TAG,"User data is changed!"+user.name+","+user.email + " "+ user.type)

                //Display newly updated nme and email
                emailwp.setText(user.email)
                usernamewp.setText(user.name)
                usertype.setText(user.type)


            }
            override fun onCancelled(error: DatabaseError){
                //Failed to read value
                Log.e(TAG,"Failed to read user",error.toException())
            }
        })
    }
    fun onLogout(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity2::class.java))
    }

}
