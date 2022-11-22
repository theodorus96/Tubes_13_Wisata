package com.example.coba.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coba.EditWisataActivity
import com.example.coba.R
import com.example.coba.api.UserApi
import com.example.coba.RVWisataAdapter
import com.example.coba.api.WisataApi
//import com.example.coba.room.Wisata
import com.example.coba.room.UserDB
import com.example.coba.models.Wisata

import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_wisata.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class FragmentWisata : Fragment() {
//    private var layoutLoading: LinearLayout? = null
    private var _binding: FragmentWisata? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
//    private val db by lazy {
//        UserDB(requireActivity())
//    }

    private val adapter : RVWisataAdapter = RVWisataAdapter(arrayListOf(), object : RVWisataAdapter.onAdapterListener{

        override fun onDelete(wisata: Wisata) {
            deleteDialog(wisata)
        }

        //untuk update 2
        override fun onUpdate(wisata :Wisata){
            intentEdit(wisata.id, 2)
        }

    })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wisata, container, false)
    }

    override fun onStart() {
        super.onStart()

    }

    fun loadData(){
        val dataWisata = db.WisataDAO().getWisata()
        println(dataWisata)
        adapter.setData(dataWisata)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)


        val rvWisata : RecyclerView = view.findViewById(R.id.rv_wisata)

        rvWisata.layoutManager = layoutManager

        rvWisata.setHasFixedSize(true)
        loadData()
        rvWisata.adapter = adapter
    }
    val rvWisata = binding.rv_wisata

    private fun allwisata(){
        binding.srWisata.isRefreshing = true
        val stringRequest: StringRequest = object :
        StringRequest(Method.GET, WisataApi.GET_ALL_URL, Response.Listener {  response ->
            val gson = Gson()
            val wisata : Array<Wisata> = gson.fromJson(response, Array<Wisata>::class.java)
            val jsonObject = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray("data")

            adapter!!.setWisataList(wisata)
            adapter!!.filter.filter(svWisata!!.query)
            srWisata!!.isRefreshing = false
            if (!wisata.isEmpty())
                Toast.makeText(requireContext(), "Data berhasil diambil", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(requireContext(), "Data Kosong", Toast.LENGTH_SHORT).show()
        },Response.ErrorListener { error ->
            try{
                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                val errors = JSONObject(responseBody)
                Toast.makeText(
                    requireContext(),
                    errors.getString("message"),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e:Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }}
        queue!!.add(stringRequest)
    }
    fun deleteDialog(id: Int){
//        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.DELETE, WisataApi.DELETE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val json = JSONObject(response)
                    val wisata = gson.fromJson(response, Wisata::class.java)

                    if(wisata != null)
                    Toast.makeText(requireContext(), "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                    allwisata()
                }, Response.ErrorListener { error ->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            requireContext(),
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e:Exception){
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers
                    }}
        queue!!.add(stringRequest)

    }

//    private fun setLoading(isLoading: Boolean){
//        if(isLoading){
//            requireActivity().window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//                binding.layoutLoading.visibility = View.VISIBLE
//        }else{
//           requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            binding.layoutLoading.visibility = View.GONE
//        }
//    }

    fun intentEdit(id : Int,intentType: Int){
        startActivity(
            Intent(requireActivity(),EditWisataActivity::class.java)
                .putExtra("intent_id",id)
                .putExtra("intent_type",intentType)
        )
    }
}