package com.example.coba.camera

import android.annotation.SuppressLint
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.coba.R
import com.vmadalin.easypermissions.EasyPermissions
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_camera.*
import java.security.Permission

class MainCamera : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null

    companion object{
        const val CAMERA_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
            requestCameraPermission()
    }

    private fun hasCameraPermission() =
        EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA)

    private fun requestCameraPermission(){
        EasyPermissions.requestPermissions(this, "This app needs access to your camera to work properly",
            CAMERA_REQUEST_CODE, android.Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toasty.error(
                this,
                "You have denied camera permission. Please allow it in app settings for additional functionality.",
                Toasty.LENGTH_LONG
            ).show()
        }else{
            requestCameraPermission()
        }
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toasty.success(this, "Permission granted", Toasty.LENGTH_SHORT).show()
        try {
            mCamera = Camera.open()
        }catch (e: Exception){
            Log.d("Error","Failed to get Camera"+ e.message)
        }
        if(mCamera != null){
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }
        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener { view: View? -> System.exit(0)}
    }

}