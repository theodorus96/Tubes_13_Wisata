package com.example.coba.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.coba.EditWisataActivity
import com.example.coba.adapter.WisataAdapter
import com.example.coba.api.WisataApi
import com.example.coba.databinding.FragmentWisataBinding
import com.example.coba.models.Wisata
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_wisata.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class FragmentWisata : Fragment() {
    private var _binding: FragmentWisataBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private var srWisata: SwipeRefreshLayout? = null
    private var svWisata: SearchView? = null
    private var adapter: WisataAdapter? = null
    private var activity1: Activity? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWisataBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        activity1 = requireActivity()
        queue = Volley.newRequestQueue(requireContext())
        srWisata?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allwisata() })

        binding.buttonAdd.setOnClickListener {
            val i = Intent(requireContext(),  EditWisataActivity::class.java)
            i.putExtra("intent_type", 1)
            startActivity(i)
        }

        val rvWisata = binding.rvWisata
        adapter = WisataAdapter(ArrayList(), this)
        rvWisata.layoutManager = LinearLayoutManager(requireContext())
        rvWisata.adapter = adapter
        allwisata()
    }

    private fun allwisata(){
        val stringRequest: StringRequest = object :
        StringRequest(Method.GET, WisataApi.GET_ALL_URL, Response.Listener {  response ->
            val gson = Gson()
            val jsonObject = JSONObject(response)
            val data = jsonObject.getJSONArray("data")
            val wisata : Array<Wisata> = gson.fromJson(data.toString(), Array<Wisata>::class.java)

            adapter!!.setWisataList(wisata)

            if (!wisata.isEmpty())
                Toast.makeText(activity1, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(activity1, "Data Kosong", Toast.LENGTH_SHORT).show()
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

    fun intentEdit(id : Int,intentType: Int){
        startActivity(
            Intent(requireActivity(),EditWisataActivity::class.java)
                .putExtra("intent_id",id)
                .putExtra("intent_type",intentType)
        )
    }

    fun deleteWisata(id: Int){
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.DELETE, WisataApi.DELETE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()

                    val wisata = gson.fromJson(response, Wisata::class.java)

                    if(wisata != null)
                        Toast.makeText(requireContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()

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
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }}
        queue!!.add(stringRequest)

    }
}