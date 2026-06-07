package com.example.easywheels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class User(val name: String, val email: String, val phone: String)

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "EasyWheelsDB", null, 2) { // 🔹 Bumped version to 2

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                email TEXT UNIQUE,
                phone TEXT,
                password TEXT
            )
        """.trimIndent()

        val createCarsTable = """
            CREATE TABLE cars(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                price TEXT,
                image TEXT,
                location TEXT,
                available INTEGER DEFAULT 1
            )
        """.trimIndent()

        val createBookingsTable = """
            CREATE TABLE bookings(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_email TEXT,
                car_name TEXT,
                location TEXT,
                start_date TEXT,
                end_date TEXT,
                transaction_id TEXT,
                status TEXT
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createCarsTable)
        db.execSQL(createBookingsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS cars")
        db.execSQL("DROP TABLE IF EXISTS bookings")
        onCreate(db)
    }

    fun insertUser(name: String, email: String, phone: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("email", email)
        values.put("phone", phone)
        values.put("password", password)
        val result = db.insert("users", null, values)
        return result != -1L
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=? AND password=?",
            arrayOf(email, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    @SuppressLint("Range")
    fun getUser(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email=?", arrayOf(email))
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("email")),
                cursor.getString(cursor.getColumnIndex("phone"))
            )
        }
        cursor.close()
        return user
    }

    fun isCarsTableEmpty(): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM cars", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count == 0
    }

    fun insertSampleCars() {
        val db = this.writableDatabase
        db.execSQL("INSERT INTO cars(name,price,image,location) VALUES('Toyota Innova','₹2500/day','innova','Kochi')")
        db.execSQL("INSERT INTO cars(name,price,image,location) VALUES('Hyundai Creta','₹2000/day','creta','Kochi')")
        db.execSQL("INSERT INTO cars(name,price,image,location) VALUES('Toyota Fortuner','₹3500/day','fortuner','Kochi')")
        db.execSQL("INSERT INTO cars(name,price,image,location) VALUES('Maruti Dzire','₹1500/day','dzire','Kochi')")
        db.execSQL("INSERT INTO cars(name,price,image,location) VALUES('Honda City','₹2200/day','city','Trivandrum')")
        db.execSQL("INSERT INTO cars(name,price,image,location) VALUES('Maruti Swift','₹1200/day','swift','Calicut')")
    }

    fun getCarsByLocation(location: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM cars WHERE location=?", arrayOf(location))
    }

    fun getAllCars(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM cars", null)
    }

    fun insertBooking(userEmail: String, carName: String, location: String, startDate: String, endDate: String, transactionId: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("user_email", userEmail)
        values.put("car_name", carName)
        values.put("location", location)
        values.put("start_date", startDate)
        values.put("end_date", endDate)
        values.put("transaction_id", transactionId)
        values.put("status", "Pending")
        return db.insert("bookings", null, values) != -1L
    }

    fun getUserBookings(email: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM bookings WHERE user_email=?", arrayOf(email))
    }

    fun getAllBookings(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM bookings", null)
    }

    fun updateBookingStatus(id: Int, status: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("status", status)
        return db.update("bookings", values, "id=?", arrayOf(id.toString())) > 0
    }

    fun addCar(name: String, price: String, image: String, location: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("price", price)
        values.put("image", image)
        values.put("location", location)
        values.put("available", 1)
        return db.insert("cars", null, values) != -1L
    }

    fun deleteCar(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("cars", "id=?", arrayOf(id.toString())) > 0
    }

    fun updateCarAvailability(id: Int, available: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("available", available)
        return db.update("cars", values, "id=?", arrayOf(id.toString())) > 0
    }
}