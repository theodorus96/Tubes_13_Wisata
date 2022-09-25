package com.example.coba.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coba.EditWisataActivity
import com.example.coba.R
import com.example.coba.RVWisataAdapter
import com.example.coba.room.Wisata
import com.example.coba.room.UserDB

class FragmentWisata : Fragment() {
    private val db by lazy {
        UserDB(requireActivity())
    }

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
        setupListener()
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

    fun deleteDialog(wisata: Wisata){
        db.WisataDAO().deleteWisata(wisata)
    }
    fun setupListener(){
        val btnAdd= requireActivity().findViewById<Button>(R.id.button_add)
        btnAdd.setOnClickListener {
            intentEdit(0,1)
        }
    }
    fun intentEdit(id : Int,intentType: Int){
        startActivity(
            Intent(requireActivity(),EditWisataActivity::class.java)
                .putExtra("intent_id",id)
                .putExtra("intent_type",intentType)
        )
    }
}