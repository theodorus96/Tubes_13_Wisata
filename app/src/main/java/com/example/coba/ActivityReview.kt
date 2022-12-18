package com.example.coba

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.coba.adapter.ReviewAdapter
import com.example.coba.api.ReviewApi
import com.example.coba.fragment.FragmentHome
import com.example.coba.fragment.FragmentProfil
import com.example.coba.fragment.FragmentWisata
import com.example.coba.map.MainMap
import com.example.coba.models.Review
import com.example.coba.models.Wisata
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_review.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ActivityReview : AppCompatActivity() {
    private var srReview: SwipeRefreshLayout? = null
    private var adapter: ReviewAdapter? = null
    private var svReview: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    lateinit var bottomNav : BottomNavigationView
    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        queue = Volley.newRequestQueue(this)
        srReview = findViewById(R.id.sr_review)
        svReview = findViewById(R.id.sv_review)

        srReview?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allReview() })
        svReview?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter!!.filter.filter(p0)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val i = Intent(this@ActivityReview , AddEditActivityReview::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_review)
        adapter = ReviewAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allReview()

        bottom_navigation.selectedItemId = R.id.review
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
                    val moveReview = Intent(this@ActivityReview, ActivityReview::class.java)
                    startActivity(moveReview)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.map -> {
                    val moveMap = Intent(this@ActivityReview, MainMap::class.java)
                    startActivity(moveMap)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_exit -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@ActivityReview)
                    builder.setMessage("Are you sure want to exit?")
                        .setNegativeButton("YES", object : DialogInterface.OnClickListener {
                            override fun onClick(dialogInterface: DialogInterface, i:Int){
                                val logout = Intent(this@ActivityReview, MainActivity::class.java)
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

    private fun allReview(){
        srReview!!.isRefreshing = true
        val stringRequest : StringRequest = object:
            StringRequest(Method.GET, ReviewApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val data = jsonObject.getJSONArray("data")
                val review : Array<Review> = gson.fromJson(data.toString(), Array<Review>::class.java)

                adapter!!.setReviewList(review)

                if(!review.isEmpty())
                    Toast.makeText(this@ActivityReview, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@ActivityReview, "Data Kosong!", Toast.LENGTH_SHORT).show()

            }, Response.ErrorListener { error ->
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@ActivityReview, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(this@ActivityReview, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteReview(id: Long){
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, ReviewApi.DELETE_URL+id, Response.Listener { response ->

                val gson = Gson()
                var review = gson.fromJson(response, Review::class.java)
                if(review != null)
                    Toast.makeText(this@ActivityReview, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()

                allReview()
            }, Response.ErrorListener { error ->

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@ActivityReview, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this@ActivityReview, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                allReview()
            }
        }
    }

}