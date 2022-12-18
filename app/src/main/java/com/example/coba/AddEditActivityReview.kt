package com.example.coba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.coba.api.ReviewApi
import com.example.coba.models.Review
import com.example.coba.models.Wisata
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_wisata.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditActivityReview : AppCompatActivity() {

    private var etNama: EditText? = null
    private var etKomentar: EditText? = null
    private var etNilai:  EditText? = null
    private var etSaran:  EditText? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_review)

        queue = Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etKomentar = findViewById(R.id.et_komentar)
        etNilai = findViewById(R.id.et_nilai)
        etSaran = findViewById(R.id.et_saran)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if(id== -1L){
            tvTitle.setText("Tambah Review")
            btnSave.setOnClickListener { createReview() }
        }else{
            tvTitle.setText("Edit Review")
            getReviewById(id)

            btnSave.setOnClickListener { updateReview(id) }
        }
    }

    private fun getReviewById(id: Long){
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, ReviewApi.GET_BY_ID_URL + id,
                { response ->
                    val gson = Gson()
                    val jsonObject = JSONObject(response)
                    val data = jsonObject.getJSONArray("data")
                    val review : Array<Review> = gson.fromJson(data.toString(), Array<Review>::class.java)

                    etNama!!.setText(review[0].nama)
                    etKomentar!!.setText(review[0].komentar)
                    etNilai!!.setText(review[0].nilai)
                    etSaran!!.setText(review[0].saran)

                    Toast.makeText(this@AddEditActivityReview,"Data berhasil diambil", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener{ error ->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(this@AddEditActivityReview, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createReview(){

            val review = Review(
                etNama!!.text.toString(),
                etKomentar!!.text.toString(),
                etNilai!!.text.toString(),
                etSaran!!.text.toString()
            )
            val stringRequest: StringRequest =
                object: StringRequest(Method.POST, ReviewApi.ADD_URL, Response.Listener { response ->
                    val gson = Gson()
                    var review = gson.fromJson(response, Review::class.java)

                    if(review != null)
                        Toast.makeText(this@AddEditActivityReview, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()


                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                }, Response.ErrorListener { error ->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(this@AddEditActivityReview, e.message, Toast.LENGTH_SHORT).show()
                    }
                }){
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers
                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val gson = Gson()
                        val requestBody = gson.toJson(review)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            queue!!.add(stringRequest)

    }

    private fun updateReview(id: Long){
        val review = Review(
            etNama!!.text.toString(),
            etKomentar!!.text.toString(),
            etNilai!!.text.toString(),
            etSaran!!.text.toString()
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.PUT, ReviewApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                val review = gson.fromJson(response, Review::class.java)

                if(review != null)
                    Toast.makeText(this@AddEditActivityReview, "Data berhasil diubah", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@AddEditActivityReview, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["nama"] = review.nama
                    params["komentar"] = review.komentar
                    params["nilai"] = review.nilai
                    params["saran"] = review.saran
                    return params
                }
            }
        queue!!.add(stringRequest)
    }

}