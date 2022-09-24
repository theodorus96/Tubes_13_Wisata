package com.example.coba

import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.coba.databinding.ActivityRegisterBinding
import com.example.coba.room.Constant
import com.example.coba.room.User
import com.example.coba.room.UserDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    val db by lazy { UserDB(this) }
    var binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        val sp = getSharedPreferences("user", 0)
        val id: Int = sp.getInt("id", 0)

        binding!!.btnRegister.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val loggedUser: List<User> = db.userDao().getUser(id)
                val logged = loggedUser[0]
                db.userDao().updateUser(
                    User(
                        logged.id,
                        binding!!.etName.text.toString(),
                        binding!!.etUsername.text.toString(),
                        binding!!.etEmail.text.toString(),
                        binding!!.etBornDate.text.toString(),
                        binding!!.etPhoneNumber.text.toString(),
                    )
                )
                finish()
            }
        }
        setContentView(binding?.root)
    }
}

