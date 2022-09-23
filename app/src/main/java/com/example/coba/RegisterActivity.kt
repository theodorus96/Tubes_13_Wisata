package com.example.coba
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.coba.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
class RegisterActivity : AppCompatActivity() {

    private lateinit var vName: TextInputLayout
    private lateinit var vUsername: TextInputLayout
    private lateinit var vPassword: TextInputLayout
    private lateinit var vBorndate : TextInputLayout
    private lateinit var vPhone : TextInputLayout
    private lateinit var vEmail : TextInputLayout
    private lateinit var nameInput : TextInputEditText
    private lateinit var usernameInput : TextInputEditText
    private lateinit var passwordInput : TextInputEditText
    private lateinit var phoneInput : TextInputEditText
    private lateinit var bornDateInput : TextInputEditText
    private lateinit var emailInput : TextInputEditText


    private lateinit var mainRegister: ConstraintLayout

    private lateinit var btnRegister: Button
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_register)

        binding.btnRegister.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            val mBundle = Bundle()
            var checkRegister : Boolean = true
            val name : String = binding.etName.toString()
            val bornDate : String = binding.etBornDate.toString()
            val phone : String = binding.etPhoneNumber.toString()
            val email : String = binding.etEmail.toString()
            val username : String = binding.etUsername.toString()
            val password : String = binding.etPassword.toString()

            if(name.isEmpty()){
                nameInput.setError("Nama Tidak Boleh Kosong")
                checkRegister = false
            }
            if(username.isEmpty()){
                usernameInput.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }
            if(password.isEmpty()){
                passwordInput.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }
            if(email.isEmpty()){
                emailInput.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }
            if(bornDate.isEmpty()){
                bornDateInput.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }
            if(phone.isEmpty()){
                phoneInput.setError("Nomor Telepon Tidak Boleh Kosong")
                checkRegister = false
            }
            if(checkRegister==true) {
                mBundle.putString("nama", vName.getEditText()?.getText().toString())
                mBundle.putString("username", vUsername.getEditText()?.getText().toString())
                mBundle.putString("password", vPassword.getEditText()?.getText().toString())
                mBundle.putString("email", vEmail.getEditText()?.getText().toString())
                mBundle.putString("bornDate", vBorndate.getEditText()?.getText().toString())
                mBundle.putString("phone", vPhone.getEditText()?.getText().toString())

                intent.putExtra("register",mBundle)
                startActivity(intent)
            }

        }
    }
}