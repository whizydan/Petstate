package com.example.petstate.adapters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.properties.Delegates

class VetData{

    var name :String? = null
    var email :String? = null
    var type :String? = null

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
