package com.example.coba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.coba.room.UserDB
import com.example.coba.room.Wisata

class EditWisataActivity : AppCompatActivity() {
    val db by lazy{UserDB(this)}
    private var WisataId : Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wisata)
        setupView()
        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val btnSave : Button = findViewById(R.id.button_save)
        val btnEdit : Button = findViewById(R.id.button_update)

        val intentType = intent.getIntExtra("intent_type",0)
        when (intentType){
            1-> {
                btnEdit.visibility = View.GONE
            }
            2-> {
                btnSave.visibility = View.GONE
                setWisata()
            }
        }
    }
    fun setWisata(){
        val namaWisata : EditText = findViewById(R.id.edit_title)
        val lokasiWisata : EditText = findViewById(R.id.edit_wisata)

        WisataId = intent.getIntExtra("intent_id",0)

        val wisata = db.WisataDAO().getWisata(WisataId)!!

        namaWisata.setText(wisata.nama)
        lokasiWisata.setText(wisata.lokasi)
    }

    private fun setupListener(){
        val intent = Intent(this, HomeActivity::class.java)
        val bundle : Bundle = Bundle()

        val namaWisata : EditText = findViewById(R.id.edit_title)
        val lokasiWisata : EditText = findViewById(R.id.edit_wisata)

        val btnSave : Button = findViewById(R.id.button_save)
        val btnUpdate: Button = findViewById(R.id.button_update)

        btnSave.setOnClickListener {
            println(namaWisata.text.toString())
            println(lokasiWisata.text.toString())

            db.WisataDAO().addWisata(
                Wisata(0,namaWisata.text.toString(),lokasiWisata.text.toString())
            )

            startActivity(intent)
        }

        btnUpdate.setOnClickListener {
            println(namaWisata.text.toString())
            println(lokasiWisata.text.toString())

            db.WisataDAO().updateWisata(
                Wisata(WisataId,namaWisata.text.toString(),lokasiWisata.text.toString())
            )

            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}