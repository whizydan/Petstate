package com.example.petstate.petowner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper{

    //constructor
    SQLiteHelper(Context context,
                 String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version){
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //insertData
    public void insertData(String name, String age, String phone, byte[] image,String summary,String feed,String sleep,String behaviour,String colour,String gender,String bowel,String fur,String other){
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO RECORD VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //where "RECORD" is table name in database we will create in mainActivity

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, age);
        statement.bindString(3, phone);
        statement.bindBlob(4, image);
        statement.bindString(5, summary);
        statement.bindString(6, feed);
        statement.bindString(7, sleep);
        statement.bindString(8, behaviour);
        statement.bindString(9, colour);
        statement.bindString(10, gender);
        statement.bindString(11, bowel);
        statement.bindString(12, fur);
        statement.bindString(13, other);
        statement.executeInsert();
    }

    //updateData
    public void updateData(String name, String age, String phone, String fur, String bowel,
                           String gender, String behaviour, String sleep, String feed,
                           String colour, String summary, String other, byte[] image, int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to update record
        String sql = "UPDATE RECORD SET name=?, age=?, phone=?, fur=?, bowel=?, gender=?, behaviour=?, sleep=?, feed=?, colour=?, summary=?, other=?, image=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, age);
        statement.bindString(3, phone);
        statement.bindString(4, fur);
        statement.bindString(5, bowel);
        statement.bindString(6, gender);
        statement.bindString(7, behaviour);
        statement.bindString(8, sleep);
        statement.bindString(9, feed);
        statement.bindString(10, colour);
        statement.bindString(11, summary);
        statement.bindString(12, other);
        statement.bindBlob(13, image);
        statement.bindDouble(14, (double)id);

        statement.execute();
        database.close();
    }

    //deleteData
    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete record using id
        String sql = "DELETE FROM RECORD WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}