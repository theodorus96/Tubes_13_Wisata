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
import com.example.coba.models.User
import com.example.coba.room.UserDB
import com.google.gson.Gson
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class EditActivity : AppCompatActivity() {
    var tv: ShimmerTextView? = null
    var shimmer: Shimmer? = null

    val db by lazy { UserDB(this) }
    var sharedPreferences: SharedPreferences? = null
    private var UserId: Int = 0
    private var password: String = ""
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        queue = Volley.newRequestQueue(this)

        setupListener()
    }

    private fun setupListener() {
        val id = getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE).getInt("id",0).toLong()
        getUserById(id)
        val btnSave: Button = findViewById(R.id.btnSave)
        val photo: ImageView = findViewById(R.id.photo)

        photo.setOnClickListener {
            val photo = Intent(this, MainCamera::class.java)
            startActivity(photo)
        }

        btnSave.setOnClickListener {
            updateUser(id)
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

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, UserApi.GET_BY_ID_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val json = JSONObject(response)
                    var user = gson.fromJson(json.getJSONArray("data")[0].toString(), User::class.java)

                    namaLengkap.setText(user.nama)
                    username.setText(user.username)
                    email.setText(user.email)
                    bornDate.setText(user.borndate)
                    phoneNum.setText(user.phoneNum)

                    val tv: ShimmerTextView = findViewById(R.id.textProfile)
                    shimmer = Shimmer()
                    shimmer!!.start(tv)

                    Toasty.success(this,"Data User Berhasil Diambil", Toast.LENGTH_SHORT).show()

                },
                Response.ErrorListener{ error ->

                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toasty.error(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toasty.error(this@EditActivity ,"Data User Gagal Diambil", Toast.LENGTH_SHORT).show()
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

    private fun updateUser(id: Long){
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
                var user = gson.fromJson(response, User::class.java)

                if(user != null)
                    Toasty.success(this@EditActivity, "Data User Berhasil Diubah", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()


            }, Response.ErrorListener { error ->

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toasty.error(this@EditActivity, "Data User Gagal Diubah", Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["nama"] = user.nama
                    params["borndate"] = user.borndate
                    params["email"] = user.email
                    params["phoneNum"] = user.phoneNum
                    params["username"] = user.username
                    return params
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
}

