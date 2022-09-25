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
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //loadFragment(FragmentHome())
        changeFragment(FragmentHome())
        bottomNav= findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.profil -> {
                    changeFragment(FragmentProfil())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.wisata -> {
                    changeFragment(FragmentWisata())
                    return@setOnNavigationItemReselectedListener
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
                }
            }
        }
    }

//    override fun onCreateOptionsMenu(menu : Menu): Boolean{
//        val menuInflater = MenuInflater(this)
//        menuInflater.inflate(R.menu.home_menu,menu)
//        return true
//    }

    fun changeFragment(fragment: Fragment?){
        if(fragment != null){
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit()
        }
    }

//    fun changeActivity(activity: Class<*>) {
//        val intent = Intent(this, activity)
//        startActivity(intent)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.wisata){
//            changeFragment(FragmentWisata())
//        }
//        if(item.itemId == R.id.profil) {
//            changeFragment(FragmentProfil())
//        }else {
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
//            builder.setMessage("Are you sure want to exit?")
//                .setNegativeButton("YES", object : DialogInterface.OnClickListener {
//                    override fun onClick(dialogInterface: DialogInterface, i:Int){
//                        val logout = Intent(this@HomeActivity, MainActivity::class.java)
//                        startActivity(logout)
//                        finishAndRemoveTask()
//                    }
//                }).setPositiveButton("No", object : DialogInterface.OnClickListener {
//                    override fun onClick(dialogInterface: DialogInterface, i:Int){
//                    }
//                })
//                .show()
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    private  fun loadFragment(fragment: Fragment){
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.flFragment,fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
}