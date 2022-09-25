package com.example.coba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coba.databinding.ActivityRegisterBinding
import com.example.coba.room.User
import com.example.coba.room.UserDB


class RegisterActivity : AppCompatActivity() {

    val db by lazy { UserDB(this,) }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}