package com.example.coba.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.coba.EditActivity
import com.example.coba.HomeActivity
import com.example.coba.R
import com.example.coba.databinding.FragmentProfilBinding
import com.example.coba.room.User
import com.example.coba.room.UserDB
import kotlinx.android.synthetic.main.fragment_profil.*


class FragmentProfil : Fragment(R.layout.fragment_profil) {
    private var _binding : FragmentProfilBinding? = null
    private val binding get() = _binding!!
    var sharedPreferences: SharedPreferences? = null
    private lateinit var userDB: UserDB
    val db by lazy { UserDB(activity as HomeActivity) }
    private var UserId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUser(view)
        btnUpdate.setOnClickListener {
//            (activity as HomeActivity).changeActivity(EditActivity::class.java)
            val intent  = Intent(this.requireActivity(),EditActivity::class.java)
            startActivity(intent)
        }
    }

    fun setUser(view: View) {
        val namaLengkap: EditText = view.findViewById(R.id.etName)
        val username: EditText = view.findViewById(R.id.etUsername)
        val email: EditText = view.findViewById(R.id.etEmail)
        val bornDate: EditText = view.findViewById(R.id.etBornDate)
        val phoneNum: EditText = view.findViewById(R.id.etPhoneNumber)
        sharedPreferences = activity?.getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
        val id = sharedPreferences!!.getInt("id",0)


        val user: User = db.userDao().getUser(id)!!
        namaLengkap.setText(user.nama)
        username.setText(user.username)
        email.setText(user.email)
        bornDate.setText(user.borndate)
        phoneNum.setText(user.nama)

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfil().apply {
                arguments = Bundle().apply {

                }
            }
    }
}