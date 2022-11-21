package com.example.coba

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.coba.api.UserApi
import com.example.coba.camera.MainCamera
import com.example.coba.map.MainMap
import com.example.coba.models.User
import com.example.coba.room.UserDB
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class EditActivity : AppCompatActivity() {

    val db by lazy { UserDB(this) }
    var sharedPreferences: SharedPreferences? = null
    private var UserId: Int = 0
    private var password: String = ""
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupListener()
        //setUser()
        queue = Volley.newRequestQueue(this)

    }



//    fun setUser() {
//        val namaLengkap: EditText = findViewById(R.id.etName)
//        val username: EditText = findViewById(R.id.etUsername)
//        val email: EditText = findViewById(R.id.etEmail)
//        val bornDate: EditText = findViewById(R.id.etBornDate)
//        val phoneNum: EditText = findViewById(R.id.etPhoneNumber)
//
//        sharedPreferences = getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
//
//        UserId = sharedPreferences!!.getInt("id",0)
////        val user: User = db.userDao().getUser(UserId)!!
//        password = user.password
//        namaLengkap.setText(user.nama)
//        username.setText(user.username)
//        email.setText(user.email)
//        bornDate.setText(user.borndate)
//        phoneNum.setText(user.phoneNum)
//    }

    private fun setupListener() {
        val id = intent.getLongExtra("id", -1)
        getUserById(id)
        val btnSave: Button = findViewById(R.id.btnSave)
        val photo: ImageView = findViewById(R.id.photo)

        photo.setOnClickListener {
            val photo = Intent(this, MainCamera::class.java)
            startActivity(photo)
        }

        btnSave.setOnClickListener {
            updateMahasiswa(id)
 //           db.userDao().updateUser(
//                User(
//                    UserId,
//                    namaLengkap.text.toString(),
//                    bornDate.text.toString(),
//                    email.text.toString(),
//                    phoneNum.text.toString(),
//                    username.text.toString(),
//                    password
//                )
          //  )

            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getUserById(id: Long){
        val namaLengkap: EditText = findViewById(R.id.etName)
        val username: EditText = findViewById(R.id.etUsername)
        val email: EditText = findViewById(R.id.etEmail)
        val bornDate: EditText = findViewById(R.id.etBornDate)
        val phoneNum: EditText = findViewById(R.id.etPhoneNumber)
//        val btnSave: Button = findViewById(R.id.btnSave)
//        val photo: ImageView = findViewById(R.id.photo)

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, UserApi.GET_BY_ID_URL + id,
                Response.Listener { response ->
                    val user = Gson().fromJson(response, User::class.java)
                    namaLengkap!!.setText(user.nama)
                    username!!.setText(user.username)
                    email!!.setText(user.email)
                    bornDate!!.setText(user.borndate)
                    phoneNum!!.setText(user.phoneNum)

                    Toast.makeText(this@EditActivity,"Data berhasil diambil", Toast.LENGTH_SHORT).show()

                },
                Response.ErrorListener{ error ->

                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(this@EditActivity ,e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)

    }

    private fun updateMahasiswa(id: Long){
        val namaLengkap: EditText = findViewById(R.id.etName)
        val username: EditText = findViewById(R.id.etUsername)
        val email: EditText = findViewById(R.id.etEmail)
        val bornDate: EditText = findViewById(R.id.etBornDate)
        val phoneNum: EditText = findViewById(R.id.etPhoneNumber)

        val user = User(
            namaLengkap!!.text.toString(),
            bornDate!!.text.toString(),
            email!!.text.toString(),
            phoneNum!!.text.toString(),
            username!!.text.toString(),
            password,
            )
        val stringRequest: StringRequest =
            object: StringRequest(Method.PUT, UserApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var mahasiswa = gson.fromJson(response, User::class.java)

                if(mahasiswa != null)
                    Toast.makeText(this@EditActivity, "Data berhasil diubah", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()


            }, Response.ErrorListener { error ->

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@EditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
}

