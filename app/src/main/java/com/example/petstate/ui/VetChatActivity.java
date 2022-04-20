package com.example.petstate.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petstate.R;
import com.example.petstate.messenger.TinyDB;
import com.example.petstate.messenger.adapters.MainAdapter;
import com.example.petstate.messenger.models.Message;
import com.example.petstate.messenger.utils.ProfanityFilter;
import com.example.petstate.messenger.utils.SCUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VetChatActivity extends AppCompatActivity {

  public static final int ANTI_FLOOD_SECONDS = 3; //simple anti-flood
  private boolean IS_ADMIN = false; //set this to true for the admin app.
  private String username = "anonymous"; //default username
  private boolean PROFANITY_FILTER_ACTIVE = true;
  private FirebaseDatabase database;
  private RecyclerView main_recycler_view;
  private String userID;
  private VetChatActivity mContext;
  private MainAdapter adapter;
  private DatabaseReference databaseRef;
  private ImageButton imageButton_send;
  private EditText editText_message;
  ArrayList<Message> messageArrayList = new ArrayList<>();
  private ProgressBar progressBar;
  private long last_message_timestamp = 0;
  private String vetid = FirebaseAuth.getInstance().getUid();


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    TinyDB tinydb = new TinyDB(getApplicationContext());
    mContext = VetChatActivity.this;
    main_recycler_view = (RecyclerView) findViewById(R.id.main_recycler_view);
    imageButton_send = (ImageButton) findViewById(R.id.imageButton_send);
    editText_message = (EditText) findViewById(R.id.editText_message);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    database = FirebaseDatabase.getInstance();
    databaseRef = database.getReference();


    progressBar.setVisibility(View.VISIBLE);
    main_recycler_view.setLayoutManager(new LinearLayoutManager(this));
    adapter = new MainAdapter(mContext, messageArrayList);
    main_recycler_view.setAdapter(adapter);

    databaseRef.child(vetid).limitToLast(50).addChildEventListener(new ChildEventListener() {
      @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        progressBar.setVisibility(View.GONE);
        Message new_message = dataSnapshot.getValue(Message.class);
        messageArrayList.add(new_message);
        adapter.notifyDataSetChanged();
        main_recycler_view.scrollToPosition(adapter.getItemCount() - 1);
      }

      @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

      }

      @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d("REMOVED", dataSnapshot.getValue(Message.class).toString());
        messageArrayList.remove(dataSnapshot.getValue(Message.class));
        adapter.notifyDataSetChanged();
      }

      @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

      }

      @Override public void onCancelled(DatabaseError databaseError) {

      }
    });

    imageButton_send.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        process_new_message(editText_message.getText().toString().trim(), false);
      }
    });

    editText_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND)) {
          imageButton_send.performClick();
        }
        return false;
      }
    });

    logic_for_username();
  }

  private void process_new_message(String new_message, boolean isNotification) {
    if (new_message.isEmpty()) {
      return;
    }

    //simple anti-flood protection
    if ((System.currentTimeMillis() / 1000L - last_message_timestamp) < ANTI_FLOOD_SECONDS) {
      SCUtils.showErrorSnackBar(mContext, findViewById(android.R.id.content), "You cannot send messages so fast.").show();
      return;
    }

    //yes, admins can swear ;)
    if ((PROFANITY_FILTER_ACTIVE) && (!IS_ADMIN)) {
      new_message = new_message.replaceAll(ProfanityFilter.censorWords(ProfanityFilter.ENGLISH), ":)");
    }

    editText_message.setText("");


    TinyDB tinydb = new TinyDB(getApplicationContext());
    String vetid = tinydb.getString("vetname");
    Message xmessage = new Message(userID, username, new_message, System.currentTimeMillis() / 1000L, IS_ADMIN, isNotification);
    String key = databaseRef.child(vetid).push().getKey();
    databaseRef.child(vetid).child(key).setValue(xmessage);

    last_message_timestamp = System.currentTimeMillis() / 1000L;
  }

  //Popup message with your username if none found. Change it to your liking
  private void logic_for_username() {
    userID = SCUtils.getUniqueID(getApplicationContext());
    databaseRef.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        progressBar.setVisibility(View.GONE);
        if (!dataSnapshot.exists()) {
          show_alert_username();
        } else {
          username = dataSnapshot.getValue(String.class);
          Snackbar.make(findViewById(android.R.id.content), "Logged in as " + username, Snackbar.LENGTH_SHORT).show();
        }
      }

      @Override public void onCancelled(DatabaseError databaseError) {
        Log.w("!!!", "username:onCancelled", databaseError.toException());
      }
    });
  }

  private void show_alert_username() {
    AlertDialog.Builder alertDialogUsername = new AlertDialog.Builder(mContext);
    alertDialogUsername.setMessage("Your username");
    final EditText input = new EditText(mContext);
    input.setText(username);
    alertDialogUsername.setView(input);

    alertDialogUsername.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

      @Override public void onClick(DialogInterface dialog, int id) {
        String new_username = input.getText().toString().trim();
        if ((!new_username.equals(username)) && (!username.equals("anonymous"))) {
          process_new_message(username + " changed it's nickname to " + new_username, true);
        }
        username = new_username;
        databaseRef.child("users").child(userID).setValue(username);
      }
    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

      @Override public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
      }
    });
    alertDialogUsername.show();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_chat, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      show_alert_username();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
