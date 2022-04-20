package com.example.petstate

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.example.petstate.adapters.Bottomsheet
import com.example.petstate.adapters.ClientListAdapter
import com.example.petstate.databasetools.DatabaseHandler
import com.example.petstate.databasetools.clientsDataClass
import com.example.petstate.info.help
import com.example.petstate.messenger.ChatActivity
import com.example.petstate.messenger.TinyDB
import com.example.petstate.petowner.TestData
import com.example.petstate.petowner.post.MainActivity2
import com.example.petstate.security.Login
import com.example.petstate.ui.NewClientDetails
import com.example.petstate.ui.Settings
import com.example.petstate.ui.VetChatActivity
import com.example.petstate.ui.Viewpet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var isAllFabsVisible: Boolean = false
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    @SuppressLint("RemoteViewLayout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fabShowFabs: FloatingActionButton = findViewById(com.example.petstate.R.id.fabShowFabs)
        val fabDelete: FloatingActionButton = findViewById(R.id.fabDeleteInfo)
        val fabAnalysis: FloatingActionButton = findViewById(com.example.petstate.R.id.fabAnalysis)
        val fabAddClient: FloatingActionButton = findViewById(com.example.petstate.R.id.fabAddClient)
        val database = FirebaseDatabase.getInstance()
        val databaseRef = database.getReference()
        val uid = FirebaseAuth.getInstance().uid.toString()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, ChatActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val tinydb = TinyDB(applicationContext)
        tinydb.putString("vetname",uid)
        // RemoteViews are used to use the content of
        // some different layout apart from the current activity layout
        val contentView = RemoteViews(packageName, R.layout.message)
        val listview = findViewById<ListView>(R.id.listViewClients)


        databaseRef.child(uid).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousKey: String?) {
                tinydb.putString("chattingwithvet","true")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(this@MainActivity, channelId)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentIntent(pendingIntent)
                } else {

                    builder = Notification.Builder(this@MainActivity)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentIntent(pendingIntent)
                }
                notificationManager.notify(1234, builder.build())

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        materialToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.LogOut -> {
                    signOut()
                    finish()
                    true
                }
                R.id.Help -> {
                    val help = Intent(this,help::class.java)
                    startActivity(help)
                    true
                }
                R.id.Share -> {
                    share_text()
                    true
                }
                R.id.About -> {
                    val bottomsheet = Bottomsheet()
                    bottomsheet.show(getSupportFragmentManager(),"about page")
                    true

                }
                else -> false
            }


        }

        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val client: List<clientsDataClass> = databaseHandler.readClient()
        //val empArrayId = Array<String>(emp.size) { "0" }

        var clientArrayId = Array<String>(client.size){"0"}
        val clientArrayName = Array<String>(client.size) { "null" }
        val clientArrayAddress = Array<String>(client.size) { "null" }
        val clientArrayEmail =Array<String>(client.size){"null"}
         val clientArrayPhoneNo =Array<String>(client.size){"null"}
        var index = 0
        client.forEach { c ->
           // clientArrayId[index] = c.client_id.toString()
            clientArrayName[index] = c.clientName
            clientArrayAddress[index] = c.clientAddress
            clientArrayEmail[index] =c.clientEmail
            clientArrayPhoneNo[index]=c.phoneNumber
            index++
        }
        //creating custom ArrayAdapter
        val thisListAdapter = ClientListAdapter(
            this,

            clientArrayName,
            clientArrayAddress,
            )

        listview.adapter = thisListAdapter
        listview.setOnItemClickListener { parent, _, position, _ ->
            val element = parent.getItemIdAtPosition(position)
            val intent = Intent(this, Viewpet::class.java)
            intent.putExtra("client_id",element.toString())
            startActivity(intent)
        }


        //fab to show other fabs and go to add new client
        fabShowFabs.setOnClickListener() {
            if (isAllFabsVisible == false) {
                isAllFabsVisible = true
                fabDelete.visibility = FloatingActionButton.VISIBLE
                fabAnalysis.visibility = FloatingActionButton.VISIBLE
                fabAddClient.visibility = FloatingActionButton.VISIBLE
            } else {
                isAllFabsVisible = false
                fabDelete.visibility = FloatingActionButton.GONE
                fabAnalysis.visibility = FloatingActionButton.GONE
                fabAddClient.visibility = FloatingActionButton.GONE

            }
        }
        fabDelete.setOnClickListener() {
            isAllFabsVisible = false
            fabDelete.visibility = FloatingActionButton.GONE
            fabAnalysis.visibility = FloatingActionButton.GONE
            fabAddClient.visibility = FloatingActionButton.GONE
            val intent = Intent(this, VetChatActivity::class.java)
            startActivity(intent)

        }
        fabAnalysis.setOnClickListener() {
            isAllFabsVisible = false
            fabDelete.visibility = FloatingActionButton.GONE
            fabAnalysis.visibility = FloatingActionButton.GONE
            fabAddClient.visibility = FloatingActionButton.GONE
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }
        fabAddClient.setOnClickListener() {
            fabDelete.visibility = FloatingActionButton.GONE
            fabAnalysis.visibility = FloatingActionButton.GONE
            fabAddClient.visibility = FloatingActionButton.GONE
            val intent = Intent(this, NewClientDetails::class.java)

            startActivity(intent)
        }

        }

    private fun share_text(){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hey,check out this great app from the petstate group.it's definitely one of the best" +
                    "apps \uD83D\uDE09")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun signOut() {
        var mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putString("type","vet")
        editor.commit()
        val intent = Intent(this,Login::class.java)
        startActivity(intent)
        finish()
    }
}



