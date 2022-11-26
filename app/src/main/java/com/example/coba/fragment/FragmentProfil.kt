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
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.coba.EditActivity
import com.example.coba.HomeActivity
import com.example.coba.R
import com.example.coba.api.UserApi
import com.example.coba.databinding.FragmentProfilBinding
import com.example.coba.models.User
import com.example.coba.room.UserDB
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_profil.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class FragmentProfil : Fragment(R.layout.fragment_profil) {
    private var _binding : FragmentProfilBinding? = null
    private val binding get() = _binding!!
    var sharedPreferences: SharedPreferences? = null
    private lateinit var userDB: UserDB
    val db by lazy { UserDB(activity as HomeActivity) }
    private var UserId: Int = 0
    private var queue: RequestQueue? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(requireActivity())
        btnUpdate.setOnClickListener {
            val intent  = Intent(this.requireActivity(),EditActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        setUser(requireView())
    }

    fun setUser(view: View) {
        val namaLengkap: EditText = view.findViewById(R.id.etName)
        val username: EditText = view.findViewById(R.id.etUsername)
        val email: EditText = view.findViewById(R.id.etEmail)
        val bornDate: EditText = view.findViewById(R.id.etBornDate)
        val phoneNum: EditText = view.findViewById(R.id.etPhoneNumber)
        sharedPreferences = activity?.getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
        val id = sharedPreferences!!.getInt("id",0)

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

                    Toasty.success(requireActivity(),"Data User Berhasil Diambil", Toast.LENGTH_SHORT).show()

                },
                Response.ErrorListener{ error ->

                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toasty.error(
                            requireActivity(),
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toasty.error(requireActivity() ,"Data User Gagal Diambil", Toast.LENGTH_SHORT).show()
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfil().apply {
                arguments = Bundle().apply {

                }
            }
    }
}