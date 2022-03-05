package com.example.musicplayer.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.musicplayer.R
import com.example.musicplayer.utils.Constants

class SplashScreen : AppCompatActivity() {
    private val permission = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //check permission access storage
        if (checkPermission()) {
            goHome()
        } else {
            ActivityCompat.requestPermissions(this, permission, Constants.PERMISSION_CODE)
        }
    }

    private fun checkPermission(): Boolean {
        for (perms in permission) {
            val data = application.checkCallingOrSelfPermission(perms)
            if (data != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSION_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                goHome()
            } else {
                //go app details settings
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    )
                )
                finish()
            }
        }
    }


    private fun goHome() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}