package com.example.coba

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.coba.room.UserDB
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var  mBundle: Bundle
    lateinit var vUsername: String
    lateinit var vPassword : String
    val db by lazy { UserDB (this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        getBundle()

        btnRegister.setOnClickListener {
            val moveRegister = Intent( this@MainActivity,RegisterActivity::class.java)
            startActivity(moveRegister)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = true
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin=false
            }

            if (password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin=false
            }
            if (username.isEmpty()==false && password.isEmpty()==false) {
                    val users = db.userDao().getUser(username, password)



                if (users != null) {
                    val moveHome = Intent(this@MainActivity, HomeActivity::class.java)
                    val sp = getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
                    val editor = sp.edit()

                    editor.apply {
                        putInt("id", users.id)
                        putString("username", users.username)
                        putString("password", users.password)
                    }.apply()

                    startActivity(moveHome)
                }
            }
        })
    }
    fun getBundle(){
        val sp = getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
        val username = sp.getString("username","")
        val password = sp.getString("password","")

        inputUsername.editText?.setText(username)
        inputPassword.editText?.setText(password)
    }

}