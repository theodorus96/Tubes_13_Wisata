package com.example.coba

import android.os.Bundle
import android.provider.SyncStateContract
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.coba.room.Constant
import com.example.coba.room.User
import com.example.coba.room.UserDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    val db by lazy {UserDB(this)}
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()
    }

        fun setupView(){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            val intentType = intent.getIntExtra("intent type", 0)
            when (intentType){
                Constant.TYPE_CREATE -> {
                    btnUpdate.visibility =  View.GONE
                }
                Constant.TYPE_UPDATE -> {
                    btnUpdate.visibility = View.GONE
                    getUser()
                }
            }
        }

        private fun setupListener(){
            btnUpdate.setOnClickListener{
                CoroutineScope(Dispatchers.IO).launch {
                    db.userDao().updateUser(
                        User(id, etName.text.toString(), etUsername.text.toString(), etEmail.text.toString(), etBornDate.text.toString(),
                            etPhoneNumber.text.toString())
                    )
                    finish()
                }
            }
        }

        fun getUser(){
            id = intent.getIntExtra("intent id", 0)
            CoroutineScope(Dispatchers.IO).launch {
                val user = db.userDao().getUser(id)[0]
                etName.setText(user.nama)
                etUsername.setText(user.username)
                etEmail.setText(user.email)
                etBornDate.setText(user.borndate)
                etPhoneNumber.setText(user.phonenumber)
            }
        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
     }
}

