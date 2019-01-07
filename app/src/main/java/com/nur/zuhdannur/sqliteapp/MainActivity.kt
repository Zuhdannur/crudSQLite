
package com.nur.zuhdannur.sqliteapp

import android.app.Dialog
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import com.nur.zuhdannur.sqliteapp.Adapter.MahasiswaAdapter
import com.nur.zuhdannur.sqliteapp.DB.DbHelper
import com.nur.zuhdannur.sqliteapp.Model.Mahasiswa
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.form_mahasiswa.*

class MainActivity : AppCompatActivity() {

    lateinit var helper: DbHelper
    lateinit var mRunnable:Runnable
    lateinit var adapter:MahasiswaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        helper = DbHelper(this)
        checking()
        listData.layoutManager = LinearLayoutManager(this)
        adapter = MahasiswaAdapter(this,helper.readData())
        listData.adapter = adapter
        refresh.setOnRefreshListener {
           listData.adapter = MahasiswaAdapter(this,helper.readData())
           refresh.isRefreshing = false
        }

        add.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.form_mahasiswa)
            dialog.setTitle("FORM")

            val button = dialog.simpan
            val cancel  = dialog.cancel
            button.setOnClickListener {
                val nim = dialog.nim.text.toString()
                val nama = dialog.nama_mahasiswa.text.toString()
                val kelas = dialog.kelas.text.toString()
                helper.insert(Mahasiswa(helper.getLastID()!!+1,nim,nama,kelas))
                Toast.makeText(applicationContext,"Inserted Data",Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search,menu)
        var search = menu!!.findItem(R.id.search)
        var searchView = MenuItemCompat.getActionView(search) as android.support.v7.widget.SearchView
        search(searchView)
        return super.onCreateOptionsMenu(menu)

    }

    fun checking() : Boolean{
        if(helper.readData().count() == 0){
            init()
            Toast.makeText(applicationContext,"Sync Database",Toast.LENGTH_LONG).show()
            return true
        }
        else{
            return false
        }
    }

    fun init(){
        helper.insert(Mahasiswa(1,"1617116697","Zuhdan Nur Ihsan","XII RPL 1"))
        helper.insert(Mahasiswa(2,"1617116699","Ronny","XII RPL 2"))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }
    fun search(search:android.support.v7.widget.SearchView){
        search.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter!!.filter(newText)
                return true
            }

        })
    }
}
