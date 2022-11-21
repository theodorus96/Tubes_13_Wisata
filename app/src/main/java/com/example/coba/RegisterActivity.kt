package com.example.coba

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.coba.api.UserApi
import com.example.coba.databinding.ActivityRegisterBinding
import com.example.coba.models.User
import com.example.coba.room.UserDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class RegisterActivity : AppCompatActivity() {

    val db by lazy { UserDB(this,) }
    private lateinit var binding: ActivityRegisterBinding

    //Notifikasi
    //private var binding: ActivityRegisterBinding? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(this)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        binding.btnRegister.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            val mBundle = Bundle()
            var checkRegister = true
            val name : String = binding.etName.toString()
            val bornDate : String = binding.etBornDate.toString()
            val phone : String = binding.etPhoneNumber.toString()
            val email : String = binding.etEmail.toString()
            val username : String = binding.etUsername.toString()
            val password : String = binding.etPassword.toString()

            if(name.isEmpty()){
                binding.etName.setError("Nama Tidak Boleh Kosong")
                checkRegister = false
            }
            if(username.isEmpty()){
                binding.etUsername.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }
            if(password.isEmpty()){
                binding.etPassword.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }
            if(email.isEmpty()){
                binding.etEmail.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }
            if(bornDate.isEmpty()){
                binding.etBornDate.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }
            if(phone.isEmpty()){
                binding.etPhoneNumber.setError("Nomor Telepon Tidak Boleh Kosong")
                checkRegister = false
            }
            if(checkRegister==true) {
//                val nama = binding.etName.text.toString()
//                val bornDate = binding.etBornDate.text.toString()
//                val email = binding.etEmail.text.toString()
//                val phoneNum = binding.etPhoneNumber.text.toString()
//                val username = binding.etUsername.text.toString()
//                val password = binding.etPassword.text.toString()
//                println(nama+username+password)
//                val user = User(0,nama,bornDate,email, phoneNum, username ,password)
                createUser()


//                db.userDao().addUser(user)
                //val sp=getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
//                val editor=sp.edit()
//
//                editor.apply{
//                    putInt("id",user.id)
//                    putString("username",user.username)
//                    putString("password",user.password)
//                }.apply()
//
//                mBundle.putString("nama", binding.etName.text.toString())
//                mBundle.putString("username", binding.etUsername.text.toString())
//                mBundle.putString("password", binding.etPassword.text.toString())
//                mBundle.putString("email", binding.etEmail.text.toString())
//                mBundle.putString("bornDate", binding.etBornDate.text.toString())
//                mBundle.putString("phone", binding.etPhoneNumber.text.toString())
//
//                intent.putExtra("register",mBundle)
//                startActivity(intent)
            }

        }
    }
    private fun createUser(){
        val user = User(
            binding.etName.text.toString(),
            binding.etBornDate.text.toString(),
            binding.etEmail.text.toString(),
            binding.etPhoneNumber.text.toString(),
            binding.etUsername.text.toString(),
            binding.etPassword.text.toString()
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, UserApi.ADD_URL, Response.Listener { response ->

                Toast.makeText(this@RegisterActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                sendNotifiaction()
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
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    params["password"] = user.password

                    return params
                }
            }

        queue!!.add(stringRequest)

    }

    //notifikasi
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotifiaction(){
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_IMMUTABLE)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.hiling)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_campaign_24)
            .setContentTitle("Healing Maseee")
            .setContentText("Badan butuh liburan, tapi dompet butuh lembaran")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(Color.CYAN)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Log in", pendingIntent)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1,builder.build())
        }
    }


}