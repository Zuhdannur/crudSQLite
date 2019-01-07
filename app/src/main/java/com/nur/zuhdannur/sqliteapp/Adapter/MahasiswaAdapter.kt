package com.nur.zuhdannur.sqliteapp.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import com.nur.zuhdannur.sqliteapp.DB.DbHelper
import com.nur.zuhdannur.sqliteapp.Model.Mahasiswa
import com.nur.zuhdannur.sqliteapp.R
import kotlinx.android.synthetic.main.form_mahasiswa.*
import kotlinx.android.synthetic.main.item_mahasiswa.view.*

class MahasiswaAdapter(val contex:Context,val datas: ArrayList<Mahasiswa>) : RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>(),Filterable {
     lateinit var mFilter:ArrayList<Mahasiswa>
    override fun getFilter(): Filter? {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charString = constraint.toString()
                if(charString.isEmpty()){
                    mFilter = datas
                }
                else{
                    var filtered:ArrayList<Mahasiswa> = ArrayList()
                    for (mahasiswa:Mahasiswa in datas){
                        if(mahasiswa.nim.contains(charString)){
                            filtered.add(mahasiswa)
                        }
                    }
                    mFilter = filtered
                }
                var result:FilterResults = FilterResults()
                result.values = mFilter
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mFilter = results!!.values as ArrayList<Mahasiswa>
                notifyDataSetChanged()
            }

        }

    }

    init{
        mFilter = datas
    }
    lateinit var helper:DbHelper
    lateinit var mRunnable: Runnable
    class ViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        fun binditem(mahasiswa: Mahasiswa,contex: Context,helper: DbHelper){
                itemView.nim.text = mahasiswa.nim
                itemView.nama.text = mahasiswa.nama
                itemView.kelas.text = mahasiswa.kelas
                itemView.item_data.setOnClickListener {
                    val builder = AlertDialog.Builder(contex)
                    builder.setTitle("Data")
                    builder.setMessage("Nim : ${mahasiswa.nim} \nNama : ${mahasiswa.nama} \nKelas : ${mahasiswa.kelas}")
                    builder.setPositiveButton("DELETE"){
                        dialog, which -> Toast.makeText(contex,"DELETE DATA",Toast.LENGTH_LONG).show()
                        helper.delete(mahasiswa.nim)
                        MahasiswaAdapter(contex,helper.readData()).notifyDataSetChanged()
                    }
                    builder.setNegativeButton("EDIT"){
                        dialog, which ->
                        dialog.dismiss()
                        MahasiswaAdapter(contex,helper.readData()).showDialog(mahasiswa)

                    }
                    builder.setNeutralButton("Cancel"){_,_->Toast.makeText(contex,"Cancel Dialog",Toast.LENGTH_LONG).show()}

                    run {
                        val dialog:AlertDialog = builder.create()
                        dialog.show()
                    }
                }



        }
    }

    public fun showDialog(mahasiswa: Mahasiswa){
        val dialog = Dialog(contex)
        helper = DbHelper(contex)
        dialog.setContentView(R.layout.form_mahasiswa)
        dialog.setTitle("FORM")
        dialog.nim.setText(mahasiswa.nim)
        dialog.nama_mahasiswa.setText(mahasiswa.nama)
        dialog.kelas.setText(mahasiswa.kelas)
        val button = dialog.simpan
        val cancel  = dialog.cancel
        button.setOnClickListener {
            val nim = dialog.nim.text.toString()
            val nama = dialog.nama_mahasiswa.text.toString()
            val kelas = dialog.kelas.text.toString()
            val update = Mahasiswa(mahasiswa.id,nim,nama,kelas)
            helper.updateMahasiswa(update)
            Toast.makeText(contex,"Updated Data",Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MahasiswaAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(contex).inflate(R.layout.item_mahasiswa,p0,false))
    }

    override fun getItemCount(): Int {
        return mFilter.size
    }

    override fun onBindViewHolder(p0: MahasiswaAdapter.ViewHolder, p1: Int) {
        helper = DbHelper(contex)
        p0.binditem(mFilter[p1],contex,helper)
    }
}