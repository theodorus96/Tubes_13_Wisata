package com.example.coba

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.coba.fragment.FragmentHome
import com.example.coba.fragment.FragmentProfil
import com.example.coba.fragment.FragmentWisata
import com.example.coba.map.MainMap
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_review.*

class HomeActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        changeFragment(FragmentHome())

        bottomNav= findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profil -> {
                    changeFragment(FragmentProfil())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.wisata -> {
                    changeFragment(FragmentWisata())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.review -> {
                    val moveReview = Intent(this@HomeActivity, ActivityReview::class.java)
                    startActivity(moveReview)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.map -> {
                    val moveMap = Intent(this@HomeActivity, MainMap::class.java)
                    startActivity(moveMap)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_exit -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
                    builder.setMessage("Are you sure want to exit?")
                        .setNegativeButton("YES", object : DialogInterface.OnClickListener {
                            override fun onClick(dialogInterface: DialogInterface, i:Int){
                                val logout = Intent(this@HomeActivity, MainActivity::class.java)
                                startActivity(logout)
                                finishAndRemoveTask()
                            }
                        }).setPositiveButton("No", object : DialogInterface.OnClickListener {
                            override fun onClick(dialogInterface: DialogInterface, i:Int){
                            }
                        })
                        .show()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    changeFragment(FragmentHome())
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
    }

    fun changeFragment(fragment: Fragment?){
        if(fragment != null){
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit()
        }
    }
}