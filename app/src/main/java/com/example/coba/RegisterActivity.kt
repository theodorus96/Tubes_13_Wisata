package com.example.coba

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.coba.api.UserApi
import com.example.coba.databinding.ActivityRegisterBinding
import com.example.coba.models.User
import com.example.coba.room.UserDB
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import androidx.annotation.RequiresApi
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class RegisterActivity : AppCompatActivity() {

    val db by lazy { UserDB(this,) }
    private lateinit var binding: ActivityRegisterBinding

    //Notifikasi
    //private var binding: ActivityRegisterBinding? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(this)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        binding.btnRegister.setOnClickListener{
            var checkRegister = true
            val name  = binding!!.etName.text.toString()
            val bornDate  = binding!!.etBornDate.text.toString()
            val phone  = binding!!.etPhoneNumber.text.toString()
            val email = binding!!.etEmail.text.toString()
            val username  = binding!!.etUsername.text.toString()
            val password = binding!!.etPassword.text.toString()

            if(name.isEmpty()){
                binding.etName.setError("Nama Tidak Boleh Kosong")
                checkRegister = false
            }
            if(username.isEmpty()){
                binding.etUsername.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }
            if(password.isEmpty()){
                binding.etPassword.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }
            if(email.isEmpty()){
                binding.etEmail.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }
            if(bornDate.isEmpty()){
                binding.etBornDate.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }
            if(phone.isEmpty()){
                binding.etPhoneNumber.setError("Nomor Telepon Tidak Boleh Kosong")
                checkRegister = false
            }
            if(checkRegister==true) {
                createUser()
                createPdf(name, bornDate, phone, email, username, password)
            }

        }
    }
    private fun createUser(){
        val user = User(
            binding.etName.text.toString(),
            binding.etBornDate.text.toString(),
            binding.etEmail.text.toString(),
            binding.etPhoneNumber.text.toString(),
            binding.etUsername.text.toString(),
            binding.etPassword.text.toString()
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, UserApi.ADD_URL, Response.Listener { response ->

                Toast.makeText(this@RegisterActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                sendNotifiaction()
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
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    params["nama"] = user.nama
                    params["borndate"] = user.borndate
                    params["email"] = user.email
                    params["phoneNum"] = user.phoneNum
                    params["username"] = user.username
                    params["password"] = user.password

                    return params
                }
            }

        queue!!.add(stringRequest)
    }

    //create pdf
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )

    private fun createPdf(name: String, bornDate: String, phone: String, email: String, username: String, password: String) {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "Member_Healing.pdf")
        FileOutputStream(file)

        //inisalisasi pembuatan PDF
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.wisata)

        //penambahan gambar pada Gambar atas
        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val namapengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                 Berikut adalah
                 Identitas Pengguna
                 """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        //proses pembuatan table
        val width = floatArrayOf(100f,100f)
        val table = Table(width)
        //pengisian table dengan data-data
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Diri")))
        table.addCell(Cell().add(Paragraph(name)))
        table.addCell(Cell().add(Paragraph("Tanggal Lahir")))
        table.addCell(Cell().add(Paragraph(bornDate)))
        table.addCell(Cell().add(Paragraph("Email")))
        table.addCell(Cell().add(Paragraph(email)))
        table.addCell(Cell().add(Paragraph("Nomor Telepon")))
        table.addCell(Cell().add(Paragraph(phone)))
        table.addCell(Cell().add(Paragraph("Username")))
        table.addCell(Cell().add(Paragraph(username)))
        table.addCell(Cell().add(Paragraph("Password")))
        table.addCell(Cell().add(Paragraph(password)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        //pembuatan QR CODE secara generate dengan bantuan IText 7
        val barcodeQRCode = BarcodeQRCode(
                    """
                    $name
                    $bornDate
                    $email
                    $phone
                    $username
                    $password
                    ${LocalDate.now().format(dateTimeFormatter)}
                    ${LocalTime.now().format(timeFormatter)}
                    """.trimIndent())
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

        document.add(image)
        document.add(namapengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        Toast.makeText(applicationContext, "PDF Created", Toast.LENGTH_SHORT).show()
    }

    //notifikasi
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotifiaction(){
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_IMMUTABLE)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.hiling)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_campaign_24)
            .setContentTitle("Healing Maseee")
            .setContentText("Badan butuh liburan, tapi dompet butuh lembaran")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(Color.CYAN)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Log in", pendingIntent)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1,builder.build())
        }
    }

}