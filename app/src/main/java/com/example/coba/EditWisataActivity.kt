package com.example.coba

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.coroutines.Runnable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.coba.api.WisataApi
import com.example.coba.room.UserDB
import com.example.coba.models.Wisata
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_edit_wisata.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditWisataActivity : AppCompatActivity() {
    val db by lazy{UserDB(this)}
    private var WisataId : Int =0
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId2 = 102
    private var queue: RequestQueue? = null
    private var layoutLoading: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wisata)
        queue = Volley.newRequestQueue(this)
        setupView()
        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val btnSave : Button = findViewById(R.id.button_save)
        val btnEdit : Button = findViewById(R.id.button_update)

        val intentType = intent.getIntExtra("intent_type",0)
        when (intentType){
            1-> {
                btnEdit.visibility = View.GONE
            }
            2-> {
                btnSave.visibility = View.GONE
            }
        }
    }

    private fun createWisata(){
        val wisata = Wisata(
            edit_title!!.text.toString(),
            edit_wisata!!.text.toString(),
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, WisataApi.ADD_URL, Response.Listener { response->
                val gson = Gson()
                val wisata = gson.fromJson(response, Wisata::class.java)

                if(wisata!=null)
                    Toasty.success(this@EditWisataActivity, "Wisata Berhasil Ditambahkan!", Toast.LENGTH_SHORT, true).show();

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                sendNotifiaction()
                sendNotification2()
                finish()

            }, Response.ErrorListener { error->

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(
                        this@EditWisataActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toasty.error(this@EditWisataActivity, "Wisata Gagal Dibuat!", Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers

                }

                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["nama"] = wisata.nama
                    params["lokasi"] = wisata.lokasi

                    return params
                }

            }
        queue!!.add(stringRequest)
    }

    private fun getWisataById(id: Long){
        // Fungsi untuk menampilkan data wisata berdasarkan id

        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, WisataApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val data = jsonObject.getJSONArray("data")
                val wisata : Array<Wisata> = gson.fromJson(data.toString(), Array<Wisata>::class.java)


                edit_title!!.setText(wisata[0].nama)
                edit_wisata!!.setText(wisata[0].lokasi)

                Toasty.success(this@EditWisataActivity, "Data Wisata Berhasil Diambil", Toast.LENGTH_SHORT).show()
            },  Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(
                        this@EditWisataActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toasty.error(this@EditWisataActivity, "Data Wisata Gagal Diambil", Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
        queue!!.add(stringRequest)
    }

    private fun updateWisata(id: Long){

        val wisata = Wisata(
            edit_title!!.text.toString(),
            edit_wisata!!.text.toString(),
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, WisataApi.UPDATE_URL + id, Response.Listener{ response ->
                val gson = Gson()

                val wisata = gson.fromJson(response, Wisata::class.java)

                if(wisata != null)
                    Toasty.success(this@EditWisataActivity, "Data Wisata Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener{ error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(
                        this@EditWisataActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toasty.error(this@EditWisataActivity, "Data Wisata Gagal Diupdate", Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers

            }

            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["nama"] = wisata.nama
                params["lokasi"] = wisata.lokasi

                return params
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)

    }

    private fun setupListener(){
        val intent2 = Intent(this, HomeActivity::class.java)
        val bundle : Bundle = Bundle()

        val namaWisata : EditText = findViewById(R.id.edit_title)
        val lokasiWisata : EditText = findViewById(R.id.edit_wisata)
        val id = intent.getLongExtra("id", -1)
        val btnSave : Button = findViewById(R.id.button_save)
        val btnUpdate: Button = findViewById(R.id.button_update)
        getWisataById(id)
        createNotificationChannel()
        btnSave.setOnClickListener {
            createWisata()
            //startActivity(intent2)
        }

        btnUpdate.setOnClickListener {
            updateWisata(id)
            startActivity(intent2)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val channel2 = NotificationChannel(CHANNEL_ID_2, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    private fun sendNotifiaction() {
        val namaWisata : EditText = findViewById(R.id.edit_title)
        val lokasiWisata : EditText = findViewById(R.id.edit_wisata)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_note_add_24)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Wisata " + namaWisata.text.toString() +
                        " ditambahkan kedalam aplikasi Healing, lokasi wisata " + namaWisata.text.toString() +
                        " terletak di " + lokasiWisata.text.toString()+
                        ". Selamat Berlibur dan nikmati hari liburmu karena memikirkan hari esok yang belum terjadi secara berlebihan hanya akan membuatamu lupa mensyukuri hari liburmu."
                ))
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1, builder.build())
        }
    }

    private fun sendNotification2() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_2).apply {
            setContentTitle("Menambahkan Wisata")
            setContentText("Wisata sedang ditambahkan")
            setSmallIcon(R.drawable.ic_baseline_location_on_24)
            setPriority(NotificationCompat.PRIORITY_HIGH)
        }
        var download = 0
        NotificationManagerCompat.from(this).apply{
            builder.setProgress(100, 0, false)
            notify(notificationId2, builder.build())

            Thread(Runnable{
                while(download < 100) {
                    Thread.sleep(250)
                    download += 10
                    runOnUiThread {
                        builder.setProgress(100, download, false)
                        notify(notificationId2, builder.build())
                        if (download == 100) {
                            builder.setContentText("Wisata telah dibuat")
                            builder.setProgress(0, 0, false)
                            notify(notificationId2, builder.build())
                        }
                    }
                }
            }).start()

        }
    }
//    private fun setLoading(isLoading: Boolean){
//        if(isLoading){
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//            layoutLoading!!.visibility = View.VISIBLE
//        }else{
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            layoutLoading!!.visibility = View.INVISIBLE
//        }
//    }
}