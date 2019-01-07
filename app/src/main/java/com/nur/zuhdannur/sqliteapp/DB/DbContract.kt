package com.nur.zuhdannur.sqliteapp.DB

import android.provider.BaseColumns

class DbContract {
        class MahasiswaEntry:BaseColumns{
            companion object {
                val TABLE_NAME = "mahasiswa"
                val COLUMN_ID = "id"
                val COLUMN_NIM = "nim_mahasiswa"
                val COLUMN_NAMA = "nama"
                val COLUMN_KELAS = "kelas"
            }
        }
}