package com.example.petstate.petowner.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class User{

    lateinit var name:String
    lateinit var email:String
    lateinit var type :String

    //Default constructor required for calls to
    //DataSnapshot.getValue(User.class)
    constructor(){

    }

    constructor(name:String,email:String,type:String){
        this.name=name
        this.email=email
        this.type=type
    }
}
