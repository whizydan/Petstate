package com.example.petstate.databasetools

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.example.petstate.EmpModelClass
import android.database.sqlite.SQLiteOpenHelper as SQLiteOpenHelper1
import kotlin.collections.List as long

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper1(context, DATABASE_NAME,null, DATABASE_VERSION){
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val TABLE_CLIENTS = "CLIENTS"
        private val TABLE_VISITS = "VISITS"
        private val TABLE_PETS  = "PETS"
        private val CLIENT_NAME="client_name"
        private val VISIT_ID = "visit_id"
        private val PET_ID = "pet_id"
        private val CLIENT_ID = "client_id"
        private val ADDRESS = "address"
        private val CLIENT_IMAGE = "client_image"
        private val PHONE_NUMBER = "phone_number"
        private val CLIENT_EMAIL = "client_email"
        private val EMAIL = "email"
        private val NAME = "name"
        private val PHONE = "phone"
        private val HOSPITAL = "hospital"
        private val VETADDRESS = "vetaddress"
        private val VETDETAILS = "VETDETAILS"
       // public val clientIdTest =1

    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db?.execSQL("PRAGMA foreign_keys = ON" )
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE "
                + TABLE_CONTACTS +
                "(" + KEY_ID +
                " INTEGER PRIMARY KEY,"
                + KEY_NAME +
                " TEXT,"
                + KEY_EMAIL +
                " TEXT" + ")"
                )

        val CREATE_CLIENTS_TABLE = ("CREATE TABLE "
                + TABLE_CLIENTS +
                "(" + CLIENT_NAME +
                " TEXT,"
                + PET_ID +
                " INTEGER ,"
                + CLIENT_ID +
                " INTEGER PRIMARY KEY,"
                + ADDRESS +
                " TEXT,"
                + CLIENT_IMAGE +
                " TEXT,"
                + PHONE_NUMBER +
                " TEXT,"
                + CLIENT_EMAIL +
                " TEXT" +

                ")"
                )
        val CREATE_PETS_TABLE = ("CREATE TABLE \"PETS\" (\n" +
                "\t\"pet_name\"\tTEXT,\n" +
                "\t\"pet_id\"\tINTEGER,\n" +
                "\t\"client_id\"\tINTEGER,\n" +
                "\t\"dob\"\tTEXT,\n" +
                "\t\"colour\"\tTEXT,\n" +
                "\t\"kind\"\tTEXT,\n" +
                "\t\"breed\"\tTEXT,\n" +
                "\t\"pet_image\"\tBLOB,\n" +
                "\tPRIMARY KEY(\"pet_id\"AUTOINCREMENT)\n" +
                "\tFOREIGN KEY(\"client_id\") REFERENCES $TABLE_CLIENTS("+ CLIENT_ID+")"+
                ")"
                )
        val CREATE_USER_TABLE = ("CREATE TABLE \"USER\" (\n" +
                "\t\"uuid\"\tTEXT,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"phone_number\"\tTEXT,\n" +
                "\t\"email\"\tTEXT,\n" +
                "\t\"uuid_image\"\tTEXT\n" +
                ")"
                )
        val CREATE_VETDETAILS_TABLE = ("CREATE TABLE "+ VETDETAILS + " (\n" +
                "\t\"email\"\tTEXT,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"phone\"\tTEXT,\n" +
                "\t\"hospital\"\tTEXT,\n" +
                "\t\"address\"\tTEXT\n" +
                ")"
                )
//        val sqlClientPet = ("SELECT * FROM PETS p JOIN "+ TABLE_CLIENTS+" c ON p.client_id = c."+ CLIENT_ID+"WHERE " +
//                "p.client_id = c."+ CLIENT_ID+")")
        val CREATE_VISITS_TABLE = ("CREATE TABLE \"VISITS\" (\n" +
                "\t\"pet_id\"\tINTEGER,\n" +
                "\t\"visit_id\"\tINTEGER,\n" +
                "\t\"temperature\"\tINTEGER,\n" +
                "\t\"prescription\"\tTEXT,\n" +
                "\t\"diagnosis\"\tTEXT,\n" +
                "\t\"date\"\tTEXT,\n" +
                "\t\"weight\"\tINTEGER,\n" +
                "\tPRIMARY KEY(\"visit_id\" AUTOINCREMENT)\n" +
                "\tFOREIGN KEY(\"pet_id\") REFERENCES PETS(\"pet_id\")"+
                ")"
                )
        db?.execSQL(CREATE_CONTACTS_TABLE)
        db?.execSQL(CREATE_CLIENTS_TABLE)
        db?.execSQL(CREATE_PETS_TABLE)
        db?.execSQL(CREATE_USER_TABLE)
        db?.execSQL(CREATE_VISITS_TABLE)
        db?.execSQL(CREATE_VETDETAILS_TABLE)

    }




    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS)
        db.execSQL("DROP TABLE IF EXISTS " + "PETS")
        db.execSQL("DROP TABLE IF EXISTS " + "USER")
        db.execSQL("DROP TABLE IF EXISTS " + "VISITS")
        onCreate(db)
    }


    //methods to insert data

//    fun client(clientTest : clientsDataClass):Int{
//        val contentValues = ContentValues()
//
//        contentValues.get(clientTest.client_id)
//        val value = contentValues
//        return value
//    }

    fun createClient(client : clientsDataClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        //contentValues.put(KEY_ID, emp.userId)
        contentValues.put(CLIENT_NAME, client.clientName) // clientModelClass Name
        contentValues.put(ADDRESS, client.clientAddress) // clientModelClass Address
        contentValues.put(PHONE_NUMBER, client.phoneNumber) // clientModelClass phoneNumber
        contentValues.put(CLIENT_EMAIL, client.clientEmail) // clientModelClass email

        // Inserting Row
        val success = db.insert(TABLE_CLIENTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
       // val test = db.insert(TABLE_CLIENTS,null,contentValues)
        db.close() // Closing database connection
        return success
    }

    fun createVetList(vets : VetListDataClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        //contentValues.put(KEY_ID, emp.userId)
        contentValues.put(NAME, vets.vetName) // clientModelClass Name
        contentValues.put(VETADDRESS, vets.vetAddress) // clientModelClass Address
        contentValues.put(PHONE, vets.vetPhone) // clientModelClass phoneNumber
        contentValues.put(EMAIL, vets.vetEmail)
        contentValues.put(HOSPITAL, vets.vetHospital)// clientModelClass email

        // Inserting Row
        val success = db.insert("VETDETAILS", null, contentValues)
        //2nd argument is String containing nullColumnHack
        // val test = db.insert(TABLE_CLIENTS,null,contentValues)
        db.close() // Closing database connection
        return success
    }

    fun addEmployee(emp: EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail )// EmpModelClass Phone
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        val test = db.insert(TABLE_CLIENTS,null,contentValues)
        db.close() // Closing database connection
        return success
    }


    fun createVisit(visit : NewVisitDataClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()


        //insert data
        contentValues.put("pet_id", visit.pet_id)
        contentValues.put("temperature", visit.petTemperature)
        contentValues.put("diagnosis", visit.vetDiagnosis)
        contentValues.put("prescription", visit.vetPrescription)
        contentValues.put("weight", visit.petWeight)
        contentValues.put("date", visit.dateNow)

        val success = db.insert("VISITS", null, contentValues)
        db.close()
        return success
    }
    fun updateVisit(update : UpdateVisitDataClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        //insert data
        contentValues.put("temperature", update.updateTemperature)
        contentValues.put("diagnosis", update.updateDiagnosis)
        contentValues.put("prescription", update.updatePrescription)
        contentValues.put("weight", update.updateWeight)

        val success = db.update("VISITS", contentValues, "id=" + VISIT_ID, null)
        db.close()
        return success
    }





    fun createPet(pet : Test):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("client_id", pet.client_id)
        contentValues.put("pet_name", pet.petName) // clientModelClass Name
        contentValues.put("dob", pet.petDOB) // clientModelClass Address
        contentValues.put("colour", pet.petColour) // clientModelClass phoneNumber
        contentValues.put("breed", pet.petBreed) // clientModelClass email
        contentValues.put("kind", pet.petType)
        // Inserting Row
        val success = db.insert("PETS", null, contentValues)
        //2nd argument is String containing nullColumnHack
        //val test = db.insert("PETS",null,contentValues)
        db.close() // Closing database connection
        return success
    }


    @SuppressLint("Range")
    fun readPet(clientID: String):long<AddPetDataClass>{
        val visitList:ArrayList<AddPetDataClass> = ArrayList<AddPetDataClass>()
        var i= clientID

       // var cursor1: Cursor? = null
        //client = i
        val selectQuery = "SELECT  * FROM $TABLE_PETS WHERE client_id=$i" //+
                //p INNER JOIN $TABLE_CLIENTS c ON p.client_id = " +
       //c.client_id WHERE

        val db = this.readableDatabase

        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        //var userId: Int
        var petName: String
        var weight: String
        var temperature: String
        var prescription: String
        var diagnosis : String
        //var petName : String
        if (cursor.moveToFirst()) {
            do {
                // userId = cursor.getInt(cursor.getColumnIndex("id"))
                petName = cursor.getString(cursor.getColumnIndex("pet_name"))
                weight = cursor.getString(cursor.getColumnIndex("breed"))
                temperature= cursor.getString(cursor.getColumnIndex("dob"))
                prescription= cursor.getString(cursor.getColumnIndex("colour"))
                diagnosis= cursor.getString(cursor.getColumnIndex("kind"))

                val visit= AddPetDataClass(petType =  diagnosis, petBreed =weight, petDOB =  temperature,
                    petName = petName,
                    petColour = prescription)
                visitList.add(visit)
            } while (cursor.moveToNext())
        }
        return visitList
    }

    @SuppressLint("Range")
    fun readPetForViewPetDetails(clientID: String):long<AddPetDataClass>{
        val visitList:ArrayList<AddPetDataClass> = ArrayList<AddPetDataClass>()
        var i= clientID

        // var cursor1: Cursor? = null
        //client = i
        val selectQuery = "SELECT  * FROM $TABLE_PETS WHERE pet_id=$i" //+
        //p INNER JOIN $TABLE_CLIENTS c ON p.client_id = " +
        //c.client_id WHERE

        val db = this.readableDatabase

        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        //var userId: Int
        var petName: String
        var weight: String
        var temperature: String
        var prescription: String
        var diagnosis : String
        //var petName : String
        if (cursor.moveToFirst()) {
            do {
                // userId = cursor.getInt(cursor.getColumnIndex("id"))
                petName = cursor.getString(cursor.getColumnIndex("pet_name"))
                weight = cursor.getString(cursor.getColumnIndex("breed"))
                temperature= cursor.getString(cursor.getColumnIndex("dob"))
                prescription= cursor.getString(cursor.getColumnIndex("colour"))
                diagnosis= cursor.getString(cursor.getColumnIndex("kind"))

                val visit= AddPetDataClass(petType =  diagnosis, petBreed =weight, petDOB =  temperature,
                    petName = petName,
                    petColour = prescription)
                visitList.add(visit)
            } while (cursor.moveToNext())
        }
        return visitList
    }
    //method to read data

    @SuppressLint("Range")
    fun readClientForViewPet(client_id: String):long<clientsDataClass>{
        val client_id = client_id
        val clientList:ArrayList<clientsDataClass> = ArrayList<clientsDataClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CLIENTS WHERE client_id =$client_id"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        //var userId: Int
        var clientName: String
        var clientAddress: String
        var email: String
        var phone : String
        //var petName : String
        if (cursor.moveToFirst()) {
            do {
                // userId = cursor.getInt(cursor.getColumnIndex("id"))
                clientName = cursor.getString(cursor.getColumnIndex("client_name"))
                clientAddress = cursor.getString(cursor.getColumnIndex("address"))
                email= cursor.getString(cursor.getColumnIndex("client_email"))
                phone= cursor.getString(cursor.getColumnIndex("phone_number"))
                val client= clientsDataClass(clientName= clientName, clientAddress= clientAddress ,
                    clientEmail = email,phoneNumber = phone)
                clientList.add(client)
            } while (cursor.moveToNext())
        }
        return clientList
    }
    @SuppressLint("Range")
    fun readClient():long<clientsDataClass>{
        val clientList:ArrayList<clientsDataClass> = ArrayList<clientsDataClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CLIENTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        //var userId: Int
        var clientName: String
        var clientAddress: String
        var email: String
        var phone : String
        //var petName : String
        if (cursor.moveToFirst()) {
            do {
               // userId = cursor.getInt(cursor.getColumnIndex("id"))
                clientName = cursor.getString(cursor.getColumnIndex("client_name"))
                clientAddress = cursor.getString(cursor.getColumnIndex("address"))
                email= cursor.getString(cursor.getColumnIndex("client_email"))
                phone= cursor.getString(cursor.getColumnIndex("phone_number"))
                val client= clientsDataClass(clientName= clientName, clientAddress= clientAddress ,
                    clientEmail = email,phoneNumber = phone)
                clientList.add(client)
            } while (cursor.moveToNext())
        }
        return clientList
    }

    @SuppressLint("Range")
    fun readVets(): ArrayList<VetListDataClass> {
        val vetList:ArrayList<VetListDataClass> = ArrayList<VetListDataClass>()
        val selectQuery = "SELECT  * FROM $VETDETAILS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        //var userId: Int
        var vetName: String
        var vetAddress: String
        var vetEmail: String
        var vetPhone : String
        var vetHospital : String
        //var petName : String
        if (cursor.moveToFirst()) {
            do {
                // userId = cursor.getInt(cursor.getColumnIndex("id"))
                vetName = cursor.getString(cursor.getColumnIndex("name"))
                vetAddress = cursor.getString(cursor.getColumnIndex("address"))
                vetEmail= cursor.getString(cursor.getColumnIndex("email"))
                vetPhone= cursor.getString(cursor.getColumnIndex("phone"))
                vetHospital= cursor.getString(cursor.getColumnIndex("hospital"))
                val vet = VetListDataClass(vetEmail = vetEmail, vetPhone = vetPhone, vetHospital = vetHospital,
                vetName = vetName, vetAddress = vetAddress)
                vetList.add(vet)
            } while (cursor.moveToNext())
        }
        return vetList
    }

    @SuppressLint("Range")
    fun readVisit(visitID: String):long<AddPetDataClass>{
        val visitList:ArrayList<AddPetDataClass> = ArrayList<AddPetDataClass>()
        var i= visitID

        // var cursor1: Cursor? = null
        //client = i
        val selectQuery = "SELECT  * FROM VISITS WHERE pet_id=$i"

       // val selectQuery = "SELECT  * FROM $TABLE_VISITS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        //var userId: Int
        var date: String
        var weight: String
        var temperature: String
        var prescription: String
        var diagnosis : String
        //var petName : String
        if (cursor.moveToFirst()) {
            do {
                // userId = cursor.getInt(cursor.getColumnIndex("id"))
                date = cursor.getString(cursor.getColumnIndex("visit_id"))
                weight = cursor.getString(cursor.getColumnIndex("temperature"))
                temperature= cursor.getString(cursor.getColumnIndex("prescription"))
                prescription= cursor.getString(cursor.getColumnIndex("diagnosis"))
                diagnosis= cursor.getString(cursor.getColumnIndex("date"))
                val visit= AddPetDataClass(petType =  date, petBreed =weight, petDOB =  temperature,
                    petName = prescription,
                petColour = diagnosis)
                visitList.add(visit)
            } while (cursor.moveToNext())
        }
        return visitList
    }



    @SuppressLint("Range")
    fun viewVisitRead(visit_id:String): MutableList<ViewVisitDataClass> {
        val list: MutableList<ViewVisitDataClass> = ArrayList()
        val db = this.readableDatabase
        val i=visit_id
        val query = "Select * from VISITS WHERE visit_id=$i"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val user = ViewVisitDataClass(date= result.getString(result.getColumnIndex("date")),
                        weight= result.getString(result.getColumnIndex("weight")),
                        temperature= result.getString(result.getColumnIndex("temperature")),
                        prescription= result.getString(result.getColumnIndex("prescription")),
                        diagnosis= result.getString(result.getColumnIndex("diagnosis"))




                )

                list.add(user)
            }
            while (result.moveToNext())
        }
        return list
    }
    @SuppressLint("Range")
    fun viewEmployee(): long<EmpModelClass> {
        val empList:ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                val emp= EmpModelClass(userId = userId, userName = userName, userEmail = userEmail)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }
    //method to update data
    fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail ) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    fun createReport(report : ReportDataClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        //contentValues.put(KEY_ID, emp.userId)
        contentValues.put("feeding", report.feeding) // clientModelClass Name
        contentValues.put("bowel", report.bowel) // clientModelClass Address
        contentValues.put("behaviour", report.behaviour) // clientModelClass phoneNumber
        contentValues.put("fur", report.fur) // clientModelClass email
        contentValues.put("sleeping", report.sleeping)
        contentValues.put("other", report.other)

        // Inserting Row
        val success = db.insert("SYMPTOMS", null, contentValues)
        //2nd argument is String containing nullColumnHack
        // val test = db.insert(TABLE_CLIENTS,null,contentValues)
        db.close() // Closing database connection
        return success
    }

}