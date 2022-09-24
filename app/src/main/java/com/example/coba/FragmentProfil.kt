package com.example.coba

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coba.databinding.FragmentProfilBinding
import com.example.coba.room.UserDB


class FragmentProfil : Fragment(R.layout.fragment_profil) {
    private var _binding : FragmentProfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var userDB: UserDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
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