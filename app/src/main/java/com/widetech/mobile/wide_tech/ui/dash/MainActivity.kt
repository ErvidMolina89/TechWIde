package com.widetech.mobile.wide_tech.ui.dash

import `in`.myinnos.awesomeimagepicker.activities.AlbumSelectActivity
import `in`.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery
import `in`.myinnos.awesomeimagepicker.models.Image
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kosalgeek.android.photoutil.ImageLoader
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.Base.BaseActivity
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.OptionCamera
import com.widetech.mobile.wide_tech.ui.notifications.PerfilFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.mContext = this
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_products,
                R.id.navigation_contacts,
                R.id.navigation_perfil
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
