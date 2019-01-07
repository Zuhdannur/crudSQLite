package com.nur.zuhdannur.sqliteapp.DB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.nur.zuhdannur.sqliteapp.Model.Mahasiswa

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABSE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
           db!!.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    public fun readData() : ArrayList<Mahasiswa>{
        val data = ArrayList<Mahasiswa>()
        val db = writableDatabase
        var cursor:Cursor?
        try{
            cursor = db.rawQuery("SELECT * FROM "+DbContract.MahasiswaEntry.TABLE_NAME,null)
        }catch (e:Exception){
                db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }
        if(cursor.moveToFirst()){
            while (cursor.isAfterLast == false){
                var id = cursor.getInt(cursor.getColumnIndex(DbContract.MahasiswaEntry.COLUMN_ID))
                var nim = cursor.getString(cursor.getColumnIndex(DbContract.MahasiswaEntry.COLUMN_NIM))
                var nama = cursor.getString(cursor.getColumnIndex(DbContract.MahasiswaEntry.COLUMN_NAMA))
                var kelas = cursor.getString(cursor.getColumnIndex(DbContract.MahasiswaEntry.COLUMN_KELAS))
                data.add(Mahasiswa(id,nim,nama,kelas))
                cursor.moveToNext()
            }
        }
        return data
    }
    @Throws(SQLiteConstraintException::class)
    fun insert(mahasiswa: Mahasiswa):Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(DbContract.MahasiswaEntry.COLUMN_NIM,mahasiswa.nim)
        values.put(DbContract.MahasiswaEntry.COLUMN_NAMA,mahasiswa.nama)
        values.put(DbContract.MahasiswaEntry.COLUMN_KELAS,mahasiswa.kelas)
        val newRow = db.insert(DbContract.MahasiswaEntry.TABLE_NAME,null,values)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun delete(nim:String?) : Boolean{
        val db = writableDatabase
        val seletion = DbContract.MahasiswaEntry.COLUMN_NIM + " LIKE ?"
        val args = arrayOf(nim)
        db.delete(DbContract.MahasiswaEntry.TABLE_NAME,seletion,args)
        return true
    }

    fun getLastID() : Int?{
        val db = writableDatabase
        var cursor:Cursor = db.rawQuery("SELECT * FROM "+DbContract.MahasiswaEntry.TABLE_NAME,null)
        var id:String? = null
        if(cursor.moveToLast()){
            id = cursor.getString(0)
        }
        if(id.isNullOrEmpty()){
            id = "0"
        }
        return id!!.toInt() +1
    }

    fun updateMahasiswa(mahasiswa: Mahasiswa) : Boolean{
        val db = writableDatabase
        var values = ContentValues()
        values.put(DbContract.MahasiswaEntry.COLUMN_NIM,mahasiswa.nim)
        values.put(DbContract.MahasiswaEntry.COLUMN_NAMA,mahasiswa.nama)
        values.put(DbContract.MahasiswaEntry.COLUMN_KELAS,mahasiswa.kelas)
        val retVal = db.update(DbContract.MahasiswaEntry.TABLE_NAME,values,DbContract.MahasiswaEntry.COLUMN_ID+" = "+mahasiswa.id,null)
        if(retVal >= 1){
            return  true
        }
        else
            return false

    }


    companion object {
        val DATABASE_NAME = "db_akademik.db"
        val DATABSE_VERSION = 2
        private val SQL_CREATE_ENTRIES = "CREATE TABLE "+DbContract.MahasiswaEntry.TABLE_NAME+"( "+
                DbContract.MahasiswaEntry.COLUMN_ID+" INTEGER PRIMARY KEY,"+
                DbContract.MahasiswaEntry.COLUMN_NIM+" TEXT,"+
                DbContract.MahasiswaEntry.COLUMN_NAMA+" TEXT,"+
                DbContract.MahasiswaEntry.COLUMN_KELAS+" TEXT);"
        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+DbContract.MahasiswaEntry.TABLE_NAME
    }
}