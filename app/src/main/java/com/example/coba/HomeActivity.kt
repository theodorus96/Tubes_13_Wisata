package com.example.coba

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadFragment(FragmentHome())

        bottomNav= findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.profil -> {
                    loadFragment(FragmentProfil())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.wisata -> {
                    loadFragment(FragmentWisata())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_exit -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
                    builder.setMessage("Are you sure want to exit?")
                        .setNegativeButton("YES", object : DialogInterface.OnClickListener {
                            override fun onClick(dialogInterface: DialogInterface, i:Int){
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

    override fun onCreateOptionsMenu(menu : Menu): Boolean{
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.home_menu,menu)
        return true
    }

    fun changeFragment(fragment: Fragment?){
        if(fragment != null){
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.wisata){
            changeFragment(FragmentWisata())
        }
        if(item.itemId == R.id.profil) {
            changeFragment(FragmentProfil())
        }else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
            builder.setMessage("Are you sure want to exit?")
                .setNegativeButton("YES", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i:Int){
                        finishAndRemoveTask()
                    }
                }).setPositiveButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i:Int){
                    }
                })
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}