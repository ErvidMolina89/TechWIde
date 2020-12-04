package com.widetech.mobile.wide_tech.ui.notifications

import `in`.myinnos.awesomeimagepicker.activities.AlbumSelectActivity
import `in`.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery
import `in`.myinnos.awesomeimagepicker.models.Image
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ceiba.mobile.pruebadeingreso.Utils.encodeToBase64
import com.kosalgeek.android.photoutil.ImageLoader
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.Base.BaseFragment
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.User
import com.widetech.mobile.wide_tech.DataAccess.Repositories.RepoSynchronization
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.ImageRotationDetectionHelper
import com.widetech.mobile.wide_tech.Utils.OptionCamera
import com.widetech.mobile.wide_tech.Utils.crearDocumentoImagen
import kotlinx.android.synthetic.main.fragment_perfil.*
import java.io.File
import java.io.IOException
import java.lang.ClassCastException
import java.text.SimpleDateFormat
import java.util.*

class PerfilFragment : BaseFragment() {

    private lateinit var notificationsViewModel: PerfilViewModel
    private val CameraREQUEST: Int = 1800
    private val GalleryREQUEST: Int = 2
    var resultCodeClick: Int? = null
    var photoBase64: String? = null
    var urlGallery: String? = null
    var email: EditText? = null
    var name: EditText? = null
    var last: EditText? = null
    var ident: EditText? = null
    var image: ImageView? = null
    var btnCrear: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(PerfilViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_perfil, container, false)
        email = vista?.findViewById(R.id.editTextEmailNew)
        name = vista?.findViewById(R.id.editTextNameNew)
        last = vista?.findViewById(R.id.editTextLastNameNew)
        ident = vista?.findViewById(R.id.editTextIDNew)
        image = vista?.findViewById(R.id.ivIconoCamara)
        btnCrear = vista?.findViewById(R.id.btnCrear)
        poneEscuchadoresSelectorImagen()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun poneEscuchadoresSelectorImagen() {
        btnCrear?.setOnClickListener {
            if(photoBase64.isNullOrEmpty() || urlGallery.isNullOrEmpty()){
                if(validateFiels()){
                    val user = User()
                    user.email  = email?.text.toString()
                    user.id     = ident?.text.toString().toInt()
                    user.name   = name?.text.toString()
                    user.lastName = last?.text.toString()
                    if (photoBase64 != null || photoBase64 != "") {
                        user.image = photoBase64
                    }else{
                        user.image = urlGallery
                    }
                    RepoSynchronization().onInsertUser(App.mContext!!, user)
                }else{
                    Toast.makeText(App.mContext, "Te faltan llenar Campos para crear el usuario", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(App.mContext, "No has seleccionado una imagen", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }
        image?.setOnClickListener {
            val alert = OptionCamera(actionAlert())
            val fragmentManager = baseActivity?.fragmentManager?.beginTransaction()
            alert.show(fragmentManager, "alert Camera")
        }
    }

    inner class actionAlert: OptionCamera.AlertCameraOptions{
        override fun callCameraApp() {
            callCamera()
        }

        override fun callGalleryApp() {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, GalleryREQUEST)
        }
    }

    private fun validateFiels(): Boolean {
        var result = false
        if (email?.text.toString() != "" && name?.text.toString() != "" && last?.text.toString() != "" && ident?.text.toString() != ""){
            result = true
        }
        return result
    }

    private fun callCamera() {
        if (ContextCompat.checkSelfPermission(App.mContext!!, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED) {
            val arrayString = Array<String>(1, { Manifest.permission.CAMERA })
            ActivityCompat.requestPermissions(baseActivity!!, arrayString, CameraREQUEST)
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CameraREQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //Gallery
            GalleryREQUEST -> {
                if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

                    val images =  data.data
                    cvPhotoUser!!.setImageURI(images)
                    urlGallery = images.toString()
                    photoBase64 = ""
                }
            }
            //Camara
            CameraREQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imagesCap =  data?.extras?.get("data") as Bitmap
                    cvPhotoUser!!.setImageBitmap(imagesCap)
                    photoBase64 = encodeToBase64(imagesCap, 100)
                    urlGallery = ""
                }
            }
        }
        resultCodeClick = resultCode
    }
}