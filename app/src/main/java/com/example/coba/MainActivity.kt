package com.example.coba

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.coba.api.UserApi
import com.example.coba.models.User
import com.example.coba.room.UserDB
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var  mBundle: Bundle
    lateinit var vUsername: String
    lateinit var vPassword : String
    private var queue: RequestQueue? = null

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
        queue = Volley.newRequestQueue(this)
        //getBundle()

        btnRegister.setOnClickListener {
            val moveRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
            }
            if (username.isEmpty() == false && password.isEmpty() == false) {
                // val users = db.userDao().getUser(username, password)


                // if (users != null) {
//                    val moveHome = Intent(this@MainActivity, HomeActivity::class.java)
//                    val sp = getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
//                    val editor = sp.edit()
//
//                    editor.apply {
//                        putInt("id", users.id)
//                        putString("username", users.username)
//                        putString("password", users.password)
//                    }.apply()

//                    startActivity(moveHome)

                val stringRequest: StringRequest = object :
                    StringRequest(Method.GET, UserApi.GET_ALL_URL,
                        Response.Listener { response ->
                            val gson = Gson()

                            val jsonObject = JSONObject(response)
                            val dataArray = jsonObject.getJSONArray("data")
                            val users = gson.fromJson(dataArray.toString(), Array<User>::class.java)
                                .toList()

                            for (user in users) {
                                if (user.username == username && user.password == password) {
                                    val moveHome =
                                        Intent(this@MainActivity, HomeActivity::class.java)
                                    val sp =
                                        getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
                                    val editor = sp.edit()

                                    editor.apply {
                                        editor.putString("username", user.username)
                                        editor.putString("password", user.password)
                                    }.apply()

                                    startActivity(moveHome)
                                }
                            }
                        }, Response.ErrorListener { error ->

                            try {
                                val responseBody =
                                    String(error.networkResponse.data, StandardCharsets.UTF_8)
                                val errors = JSONObject(responseBody)
                                Toast.makeText(
                                    this@MainActivity,
                                    errors.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: java.lang.Exception) {
                                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers = java.util.HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers
                    }

                }
                queue!!.add(stringRequest)
                // }
            }
        })
    }
//    fun getBundle(){
//        val sp = getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
//        val username = sp.getString("username","")
//        val password = sp.getString("password","")
//
//        inputUsername.editText?.setText(username)
//        inputPassword.editText?.setText(password)
//    }


}