package com.example.coba

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coba.databinding.ActivityMainBinding
import com.example.coba.notification.NotificationReceiver
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

    private var binding: ActivityMainBinding? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101
    private val notificaitonId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

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

                    createNotificationChannel()
                    binding!!.btnRegister.setOnClickListener {
                        sendNotification2()
                    }

                    startActivity(moveHome)
                }
            }
        })
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 =  NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT ).apply{
                description =descriptionText
            }

            val channel2 =  NotificationChannel(CHANNEL_ID_2, name, NotificationManager.IMPORTANCE_DEFAULT ).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    fun getBundle(){
        val sp = getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
        val username = sp.getString("username","")
        val password = sp.getString("password","")

        inputUsername.editText?.setText(username)
        inputPassword.editText?.setText(password)
    }

    private fun sendNotification2(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_looks_two_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)){
            notify(notificaitonId2, builder.build())
        }
    }

}