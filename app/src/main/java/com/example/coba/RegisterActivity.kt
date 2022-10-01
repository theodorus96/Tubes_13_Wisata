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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coba.databinding.ActivityRegisterBinding
import com.example.coba.notification.NotificationReceiver
import com.example.coba.room.User
import com.example.coba.room.UserDB
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    val db by lazy { UserDB(this,) }
    private lateinit var binding: ActivityRegisterBinding

    //Notifikasi
    //private var binding: ActivityRegisterBinding? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101
    private val notificationId2 = 102


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding!!.btnRegister.setOnClickListener {
            sendNotifiaction()
        }

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
                val nama = binding.etName.text.toString()
                val bornDate = binding.etBornDate.text.toString()
                val email = binding.etEmail.text.toString()
                val phoneNum = binding.etPhoneNumber.text.toString()
                val username = binding.etUsername.text.toString()
                val password = binding.etPassword.text.toString()
                println(nama+username+password)
                val user = User(0,nama,bornDate,email, phoneNum, username ,password)
                createNotificationChannel()
                sendNotifiaction()
                db.userDao().addUser(user)
                //val sp=getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
//                val editor=sp.edit()
//
//                editor.apply{
//                    putInt("id",user.id)
//                    putString("username",user.username)
//                    putString("password",user.password)
//                }.apply()

                mBundle.putString("nama", binding.etName.text.toString())
                mBundle.putString("username", binding.etUsername.text.toString())
                mBundle.putString("password", binding.etPassword.text.toString())
                mBundle.putString("email", binding.etEmail.text.toString())
                mBundle.putString("bornDate", binding.etBornDate.text.toString())
                mBundle.putString("phone", binding.etPhoneNumber.text.toString())

                intent.putExtra("register",mBundle)
                startActivity(intent)
            }

        }
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

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, intent,0)
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