package com.example.coba

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.coba.room.User
import com.example.coba.room.UserDB




class EditActivity : AppCompatActivity() {

    val db by lazy { UserDB(this) }
    var sharedPreferences: SharedPreferences? = null
    private var UserId: Int = 0
    private var password: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupListener()
        setUser()
    }

    fun setUser() {
        val namaLengkap: EditText = findViewById(R.id.etName)
        val username: EditText = findViewById(R.id.etUsername)
        val email: EditText = findViewById(R.id.etEmail)
        val bornDate: EditText = findViewById(R.id.etBornDate)
        val phoneNum: EditText = findViewById(R.id.etPhoneNumber)

        sharedPreferences = getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)

        UserId = sharedPreferences!!.getInt("id",0)
        val user: User = db.userDao().getUser(UserId)!!
        password = user.password
        namaLengkap.setText(user.nama)
        username.setText(user.username)
        email.setText(user.email)
        bornDate.setText(user.borndate)
        phoneNum.setText(user.nama)
    }

    private fun setupListener() {
        val intent = Intent(this, HomeActivity::class.java)
        val bundle: Bundle = Bundle()

        val namaLengkap: EditText = findViewById(R.id.etName)
        val username: EditText = findViewById(R.id.etUsername)
        val email: EditText = findViewById(R.id.etEmail)
        val bornDate: EditText = findViewById(R.id.etBornDate)
        val phoneNum: EditText = findViewById(R.id.etPhoneNumber)
        val btnSave: Button = findViewById(R.id.btnSave)


        btnSave.setOnClickListener {

            db.userDao().updateUser(
                User(
                    UserId,
                    namaLengkap.text.toString(),
                    bornDate.text.toString(),
                    email.text.toString(),
                    phoneNum.text.toString(),
                    username.text.toString(),
                    password
                )
            )

            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}

