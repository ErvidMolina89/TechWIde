package com.widetech.mobile.wide_tech.ui.notifications

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.ImageRotationDetectionHelper
import com.widetech.mobile.wide_tech.Utils.OptionCamera
import kotlinx.android.synthetic.main.fragment_perfil.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PerfilFragment : BaseFragment() {

    private lateinit var notificationsViewModel: PerfilViewModel
    private val CameraREQUEST: Int = 1
    private val GalleryREQUEST: Int = 2
    var resultCodeClick: Int? = null
    var photoBase64: String? = null
    var strCamera: String? = null
    var imageFilePath: String? = null
    var ImagenRotation = ImageRotationDetectionHelper()
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
            if(validateFiels()){
                val user = User()
                user.email  = email?.text.toString()
                user.id     = ident?.text.toString().toInt()
                user.name   = name?.text.toString()
                user.lastName = last?.text.toString()
                user.image  = ""
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
            val intentCamera = Intent(App.mContext!!, AlbumSelectActivity::class.java)
            intentCamera.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 10)
            startActivityForResult(intentCamera, GalleryREQUEST)
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
            var imageFile: File? = null
            try {
                imageFile = createImageFile()
            } catch (e: IOException) {
                println("Could not create file!")
            }

            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(App.mContext!!.packageManager) != null) {
                val authorities = App.mContext!!.packageName + ".fileprovider"
                val imageUri = FileProvider.getUriForFile(App.mContext!!, authorities, imageFile!!)
                callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(callCameraIntent, CameraREQUEST)
                strCamera = imageFilePath
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            //Gallery
            2 -> {
                if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

                    val images =  data.getParcelableArrayListExtra<Image>("images")
                    val photoPath = ImageLoader.init().from(images[0].path).requestSize(100, 100).bitmap
                    cvPhotoUser!!.setImageBitmap(photoPath)

                    when(ImagenRotation.getCameraPhotoOrientation(images[0].path)){

                        0 -> {
                            cvPhotoUser!!.rotation = 0f
                        }
                        90 -> {
                            cvPhotoUser!!.rotation = 90f
                        }
                        180 -> {
                            cvPhotoUser!!.rotation = 90f
                        }
                        270 -> {
                            cvPhotoUser!!.rotation = 90f
                        }
                    }
                    photoBase64?.encodeToBase64(photoPath, 100)
                }
            }
            //Camara
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imagesCap = ImageLoader.init().from(strCamera).requestSize(100, 100).bitmap
                    cvPhotoUser!!.setImageBitmap(imagesCap)
                    when(ImagenRotation.getCameraPhotoOrientation(strCamera.toString())){
                        0 -> {
                            cvPhotoUser!!.rotation = 0f
                        }
                        90 -> {
                            cvPhotoUser!!.rotation = 90f
                        }
                        180 -> {
                            cvPhotoUser!!.rotation = 90f
                        }
                        270 -> {
                            cvPhotoUser!!.rotation = 90f
                        }
                    }
                    photoBase64?.encodeToBase64(imagesCap, 100)
                }
            }
        }
        resultCodeClick = resultCode
    }

    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File = baseActivity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        if (!storageDir.exists()) storageDir.mkdirs()
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }
}