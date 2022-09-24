package com.example.coba.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coba.R
import com.example.coba.RVWisataAdapter
import com.example.coba.entity.wisata

class FragmentWisata : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wisata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVWisataAdapter = RVWisataAdapter(wisata.listOfWisata)

        val rvWisata : RecyclerView = view.findViewById(R.id.rv_wisata)

        rvWisata.layoutManager = layoutManager

        rvWisata.setHasFixedSize(true)

        rvWisata.adapter = adapter
    }
}