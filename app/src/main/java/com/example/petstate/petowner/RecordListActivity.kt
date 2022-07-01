package com.example.petstate.petowner

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType
import com.example.petstate.PetCareInfoActivity
import com.example.petstate.R
import com.example.petstate.adapters.Bottomsheet
import com.example.petstate.adapters.VetData
import com.example.petstate.messenger.ChatActivity
import com.example.petstate.messenger.TinyDB
import com.example.petstate.security.Login
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_record_list.*
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class RecordListActivity : AppCompatActivity() {
    var mListView: ListView? = null
    var mList: ArrayList<Model>? = null
    var mAdapter: RecordListAdapter? = null
    var imageViewIcon: ImageView? = null
    lateinit var daraja: Daraja
    var builderSingle :AlertDialog.Builder? = null
    val names_of_vets: MutableList<String>? = ArrayList<String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_list)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        builderSingle = AlertDialog.Builder(this)
        val tinydb = TinyDB(applicationContext)
        tinydb.putString("auth","011")
        getdata()
        mListView = findViewById(R.id.listView)
        val editdata = findViewById<FloatingActionButton>(R.id.Editdata)
        val chat = findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        var paid = tinydb.getString("chattingwithvet")



        chat.setOnClickListener {
            val inte = Intent(this@RecordListActivity, ChatActivity::class.java)
            val vet: List<*> = tinydb.getListString("vets")
            var waiting = tinydb.getString("waiting")
            if (vet.isEmpty()) {
                MaterialAlertDialogBuilder(this@RecordListActivity)
                    .setTitle("No Vets")
                    .setMessage("No Vets available yet.Sorry fot yhe inconvinience.Check back later")
                    .setPositiveButton("GOT IT") { dialogInterface, i -> }
                    .setNegativeButton("CANCEL") { dialogInterface, i -> }
                    .show()
            }
            else {
                if (paid == "true"){
                    val vxf = tinydb.getString("vetid")
                    startActivity(inte)
                }
                else if(waiting == "true"){
                    var phone = tinydb.getString("phone")
                    var date = tinydb.getString("date")
                    getResponse(phone,date)
                }else{
                    pay()
                }

            }
        }


        toolbar2.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.ownerLogOut -> {
                    signOut()
                    true
                }
                R.id.ownerShare -> {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Check out this amazing app,you can easily get access to quality health care professionals."
                    )
                    sendIntent.type = "text/plain"
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                    true
                }
                R.id.ownerabout -> {
                    val bottomsheet = Bottomsheet()
                    bottomsheet.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.bottomsheet)
                    bottomsheet.show(getSupportFragmentManager(),"about page")
                    true

                }
                else -> false
            }


        }

        val viewdata = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        mList = ArrayList()
        mAdapter = RecordListAdapter(this, R.layout.row, mList)
        mListView?.setAdapter(mAdapter)
        mSQLiteHelper = SQLiteHelper(this, "RECORDDB.sqlite", null, 1)
        mSQLiteHelper!!.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age VARCHAR, phone VARCHAR, image BLOB, summary VARCHAR, feed VARCHAR, sleep VARCHAR, behaviour VARCHAR, colour VARCHAR, gender VARCHAR, bowel VARCHAR, fur VARCHAR, other VARCHAR)")
        //get all data from sqlite
        val cursor = mSQLiteHelper!!.getData("SELECT * FROM RECORD")
        mList!!.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val age = cursor.getString(2)
            val phone = cursor.getString(3)
            val image = cursor.getBlob(4)
            //add to list
            mList!!.add(Model(id, name, age, phone, image))
        }
        mAdapter!!.notifyDataSetChanged()
        if (mList!!.size == 0) {
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "No record found...", Toast.LENGTH_SHORT).show()
        }
        mListView?.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l -> //send data to vet
            MaterialAlertDialogBuilder(this@RecordListActivity)
                .setTitle("Send")
                .setMessage("Are you sure you want to send this data.")
                .setPositiveButton("GOT IT") { dialogInterface, i -> upload() }
                .setNegativeButton("CANCEL") { dialogInterface, i -> }
                .show()
        })
        editdata.setOnClickListener {
            val intent = Intent(this@RecordListActivity, Main::class.java)
            startActivity(intent)
            Toast.makeText(this@RecordListActivity, "Add data", Toast.LENGTH_SHORT).show()
        }
        viewdata.setOnClickListener {
            val intent = Intent(this@RecordListActivity, PetCareInfoActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@RecordListActivity, "Available data", Toast.LENGTH_SHORT).show()
        }

        //giving the owner the options after long clicking
        mListView?.setOnItemLongClickListener(OnItemLongClickListener { adapterView, view, position, l -> //alert dialog to display options of update and delete
            val items = arrayOf<CharSequence>("Update", "Delete")
            val dialog = AlertDialog.Builder(this@RecordListActivity)
            dialog.setTitle("Choose an action")
            dialog.setItems(items) { dialogInterface, i ->
                if (i == 0) {
                    //update
                    val c = Main.mSQLiteHelper.getData("SELECT id FROM RECORD")
                    val arrID = ArrayList<Int>()
                    while (c.moveToNext()) {
                        arrID.add(c.getInt(0))
                    }
                    //show update dialog
                    showDialogUpdate(this@RecordListActivity, arrID[position])
                }
                if (i == 1) {
                    //delete
                    val c = Main.mSQLiteHelper.getData("SELECT id FROM RECORD")
                    val arrID = ArrayList<Int>()
                    while (c.moveToNext()) {
                        arrID.add(c.getInt(0))
                    }
                    showDialogDelete(arrID[position])
                }
            }
            dialog.show()
            true
        })
    }

    private fun showDialogDelete(idRecord: Int) {
        val dialogDelete = AlertDialog.Builder(this@RecordListActivity)
        dialogDelete.setTitle("Warning!!")
        dialogDelete.setMessage("Are you sure to delete?")
        dialogDelete.setPositiveButton("OK") { dialogInterface, i ->
            try {
                Main.mSQLiteHelper.deleteData(idRecord)
                Toast.makeText(this@RecordListActivity, "Delete successfully", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                Log.e("error", e.message!!)
            }
            updateRecordList()
        }
        dialogDelete.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
        dialogDelete.show()
    }

    private fun showDialogUpdate(activity: Activity, position: Int) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.update_dialoguee)
        dialog.setTitle("Update")
        imageViewIcon = dialog.findViewById(R.id.imageViewRecord)
        val edtName = dialog.findViewById<EditText>(R.id.updtName)
        val edtAge = dialog.findViewById<EditText>(R.id.updtAge)
        val edtPhone = dialog.findViewById<EditText>(R.id.updtPhone)
        val edtOther = dialog.findViewById<EditText>(R.id.updtOther)
        val edtFur = dialog.findViewById<EditText>(R.id.updtFur)
        val edtBowel = dialog.findViewById<EditText>(R.id.updtBowel)
        val edtGender = dialog.findViewById<EditText>(R.id.updtGender)
        val edtBehaviour = dialog.findViewById<EditText>(R.id.updtBehaviour)
        val edtSleep = dialog.findViewById<EditText>(R.id.updtSleep)
        val edtFeed = dialog.findViewById<EditText>(R.id.updtFeed)
        val edtColour = dialog.findViewById<EditText>(R.id.updtColour)
        val edtSummary = dialog.findViewById<EditText>(R.id.updtSummary)
        val btnUpdate = dialog.findViewById<Button>(R.id.btnUpdate)

        //set width of dialog
        val width = (activity.resources.displayMetrics.widthPixels * 0.95).toInt()
        //set hieght of dialog
        val height = (activity.resources.displayMetrics.heightPixels * 0.7).toInt()
        dialog.window!!.setLayout(width, height)
        dialog.show()

        //in update dialog click image view to update image
        imageViewIcon?.setOnClickListener(View.OnClickListener { //check external storage permission
            ActivityCompat.requestPermissions(
                this@RecordListActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                888
            )
        })
        btnUpdate.setOnClickListener {
            try {
                Main.mSQLiteHelper.updateData(
                    edtName.text.toString().trim { it <= ' ' },
                    edtAge.text.toString().trim { it <= ' ' },
                    edtPhone.text.toString().trim { it <= ' ' },
                    edtFur.text.toString().trim { it <= ' ' },
                    edtBowel.text.toString().trim { it <= ' ' },
                    edtGender.text.toString().trim { it <= ' ' },
                    edtBehaviour.text.toString().trim { it <= ' ' },
                    edtSleep.text.toString().trim { it <= ' ' },
                    edtFeed.text.toString().trim { it <= ' ' },
                    edtColour.text.toString().trim { it <= ' ' },
                    edtSummary.text.toString().trim { it <= ' ' },
                    edtOther.text.toString().trim { it <= ' ' },
                    Main.imageViewToByte(imageViewIcon),
                    position
                )
                dialog.dismiss()
                Toast.makeText(applicationContext, "Update Successfull", Toast.LENGTH_SHORT).show()
            } catch (error: Exception) {
                Log.e("Update error", error.message!!)
            }
            updateRecordList()
        }
    }

    private fun updateRecordList() {
        //get all data from sqlite
        val cursor = Main.mSQLiteHelper.getData("SELECT * FROM RECORD")
        mList!!.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val age = cursor.getString(2)
            val phone = cursor.getString(3)
            val image = cursor.getBlob(4)
            mList!!.add(Model(id, name, age, phone, image))
        }
        mAdapter!!.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 888) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //gallery intent
                val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
                galleryIntent.type = "image/*"
                startActivityForResult(galleryIntent, 888)
            } else {
                Toast.makeText(
                    this,
                    "Don't have permission to access file location",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 888 && resultCode == RESULT_OK) {
            val imageUri = data!!.data
            CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                .setAspectRatio(1, 1) // image will be square
                .start(this)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                //set image choosed from gallery to image view
                imageViewIcon!!.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    public override fun onResume() {
        super.onResume()
        mSQLiteHelper = SQLiteHelper(this, "RECORDDB.sqlite", null, 1)
        mSQLiteHelper!!.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age VARCHAR, phone VARCHAR, image BLOB, summary VARCHAR, feed VARCHAR, sleep VARCHAR, behaviour VARCHAR, colour VARCHAR, gender VARCHAR, bowel VARCHAR, fur VARCHAR, other VARCHAR)")
        //get all data from sqlite
        val cursor = mSQLiteHelper!!.getData("SELECT * FROM RECORD")
        mList!!.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val age = cursor.getString(2)
            val phone = cursor.getString(3)
            val image = cursor.getBlob(4)
            //add to list
            mList!!.add(Model(id, name, age, phone, image))
        }
        mAdapter!!.notifyDataSetChanged()
        if (mList!!.size == 0) {
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "No record found...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun upload() {
        val database = FirebaseDatabase.getInstance()
        val storage = FirebaseStorage.getInstance()
        val file = File("/data/data/com.example.petstate", "/databases/RECORDDB.sqlite")
        val uri = Uri.fromFile(file)
        val auxFile = File(uri.path)
        val uid = FirebaseAuth.getInstance().uid
        val riversRef = storage.reference.child("backups/$uid")
        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.upload, null)
        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        val uploadtask = riversRef.putFile(Uri.fromFile(auxFile))
        uploadtask.addOnFailureListener { // Handle unsuccessful uploads
            Toast.makeText(applicationContext, "upload failed", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }
            .addOnSuccessListener { // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                popupWindow.dismiss()
                Toast.makeText(applicationContext, "upload success", Toast.LENGTH_SHORT).show()
            }.addOnProgressListener { // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getdata() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("users").child("vet")
        val tinydb = TinyDB(applicationContext)
        databaseReference.addValueEventListener(object : ValueEventListener {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                val mutableList: MutableList<String> = ArrayList<String>()

                snapshot.children.forEach(Consumer { dataSnapshot ->
                    val key = dataSnapshot.key
                    val vet_data = dataSnapshot.getValue(VetData::class.java)
                    val name = vet_data?.name.toString()
                    names_of_vets?.add(name)
                    mutableList.add(key!!)
                })
                tinydb.putListString("vetNames",names_of_vets as ArrayList<String>)
                tinydb.putListString("vets", mutableList as ArrayList<String?>)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    companion object {
        var mSQLiteHelper: SQLiteHelper? = null
        fun imageViewToByte(image: ImageView): ByteArray {
            val bitmap = (image.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }
    }


    fun getResponse(phonenumber:String,date:String) {
        val tinydb = TinyDB(applicationContext)
        var res  = ""
        val url = URL("https://api.kerberos.co.ke/mpesa/response.php?date=$date&phonenumber=254$phonenumber")
        val connection = url.openConnection()
        BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
            var line: String?

            while (inp.readLine().also { line = it } != null) {
                res = line.toString()
            }
        }
        if (res == "0"){
            //has paid
            val vet: List<*> = tinydb.getListString("vets")
            val vetName :List<*> = tinydb.getListString("vetNames")
            //open an activity then from that activity-set vet
            builderSingle?.setIcon(R.drawable.logo)
            builderSingle?.setTitle("Select your vet:-")
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
            vetName.forEach {
                arrayAdapter.add(it.toString())
            }

            builderSingle?.setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }
            builderSingle?.setAdapter(arrayAdapter) { dialog, which ->
                tinydb.putString("chattingwithvet", "true")
                val vetid = vet[which]
                tinydb.putString("vetname", vetid as String?)
                Toast.makeText(applicationContext,"you chose $vetid",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,ChatActivity::class.java))
            }
            builderSingle?.show()

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun pay(){
        daraja = Daraja.with(
            "axROu5wdwfWiPqABDLmaydcGOkUtphPV",
            "kH3kue1W8KdMDAtF",
            Env.SANDBOX, //for Test use Env.PRODUCTION when in production
            object : DarajaListener<AccessToken> {
                override fun onResult(accessToken: AccessToken) {

                    Toast.makeText(
                        this@RecordListActivity,
                        "MPESA TOKEN : ${accessToken.access_token}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: String) {

                }
            })
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Enter your phone number ")

        // Set up the input
        val input = EditText(this)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Safaricom phonenumber")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        val date = LocalDateTime.now()
        var year = date.year.toString()
        var month = date.monthValue.toString()
        if (month.length < 2){
            month = "0" + date.monthValue.toString()
        }
        var day = date.dayOfMonth
        var hour = date.hour
        var formateddate= year+month+day+hour
        // Set up the buttons
        builder.setPositiveButton("Pay", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            var phone = input.text.toString()
            val lnmExpress = LNMExpress(
                "174379",
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                TransactionType.CustomerPayBillOnline,
                "1",
                phone,
                "174379",
                phone,
                "https://api.kerberos.co.ke/mpesa/response.php",
                "001ABC",
                "Goods Payment"
            )

            daraja.requestMPESAExpress(lnmExpress,
                object : DarajaListener<LNMResult> {
                    override fun onResult(lnmResult: LNMResult) {

                        val tinyDB = TinyDB(applicationContext)
                        tinyDB.putString("waiting","true")
                        tinyDB.putString("phone",phone.removePrefix("0"))
                        tinyDB.putString("date",formateddate)
                        Toast.makeText(
                            this@RecordListActivity,
                            "Response here ${lnmResult.ResponseDescription}",
                            Toast.LENGTH_SHORT
                        ).show()
                        //delay then call verify
//                        Timer("launch",false).schedule(500){
//                            var p = phone.removePrefix("0")
//                            getResponse(p,formateddate)
//                        }

                    }

                    override fun onError(error: String) {

                        Toast.makeText(
                            this@RecordListActivity,
                            "Error here $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
            var p = phone.removePrefix("0")
            getResponse(p,formateddate)

        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()

    }
    private fun signOut() {
        val tinydb = TinyDB(applicationContext)
        FirebaseAuth.getInstance().signOut()
        tinydb.putString("type","")
        tinydb.putString("chattingwithvet","")
        tinydb.putString("vetname","")
        tinydb.putString("waiting","")
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }


}