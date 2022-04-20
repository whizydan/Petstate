package com.example.petstate.petowner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.petstate.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class Main extends AppCompatActivity {

    EditText mEdtName, mEdtAge, mEdtPhone, msummary, mfeed, msleep, mbehaviour, mcolour, mgender, mbowel, mfur, mother;
    Button mBtnAdd, mBtnList;
    ImageView mImageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        mEdtName = findViewById(R.id.edtName);
        mEdtAge = findViewById(R.id.edtAge);
        mEdtPhone = findViewById(R.id.edtPhone);
        mBtnAdd = findViewById(R.id.btnAdd);
        mImageView = findViewById(R.id.imageView);
        msummary = findViewById(R.id.edtsummary);
        mfeed = findViewById(R.id.edtfeed);
        msleep = findViewById(R.id.edtsleep);
        mbehaviour = findViewById(R.id.edtbehaviour);
        mcolour = findViewById(R.id.edtcolour);
        mgender = findViewById(R.id.edtgender);
        mbowel = findViewById(R.id.edtbowel);
        mfur = findViewById(R.id.edtfur);
        mother = findViewById(R.id.edtother);


        //creating database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        //creating table in database
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age VARCHAR, phone VARCHAR, image BLOB, summary VARCHAR, feed VARCHAR, sleep VARCHAR, behaviour VARCHAR, colour VARCHAR, gender VARCHAR, bowel VARCHAR, fur VARCHAR, other VARCHAR)");


        //select image by on imageview click
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read external storage permission to select image from gallery
                //runtime permission for devices android 6.0 and above
                ActivityCompat.requestPermissions(
                        Main.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        //add record to sqlite
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mSQLiteHelper.insertData(
                            mEdtName.getText().toString().trim(),
                            mEdtAge.getText().toString().trim(),
                            mEdtPhone.getText().toString().trim(),
                            imageViewToByte(mImageView),
                            msummary.getText().toString().trim(),
                            mfeed.getText().toString().trim(),
                            msleep.getText().toString().trim(),
                            mbehaviour.getText().toString().trim(),
                            mcolour.getText().toString().trim(),
                            mgender.getText().toString().trim(),
                            mbowel.getText().toString().trim(),
                            mfur.getText().toString().trim(),
                            mother.getText().toString().trim()
                    );
                    Toast.makeText(Main.this, "Added successfully", Toast.LENGTH_SHORT).show();

                    finish();
                    //reset views
                    mEdtName.setText("");
                    mEdtAge.setText("");
                    mEdtPhone.setText("");
                    msummary.setText("");
                    mfeed.setText("");
                    msleep.setText("");
                    mbehaviour.setText("");
                    mcolour.setText("");
                    mgender.setText("");
                    mbowel.setText("");
                    mfur.setText("");
                    mother.setText("");
                    mImageView.setImageResource(R.drawable.choose_up);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Main.this, "Addition failed beacause : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(this, "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1,1)// image will be square
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //set image chosen from gallery to image view
                mImageView.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}