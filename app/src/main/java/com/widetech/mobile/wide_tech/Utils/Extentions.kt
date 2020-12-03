@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.widetech.mobile.wide_tech.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.rest.Endpoints
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

private var contadorIntentos = 0
private val numeroIntentos = 3
fun Context.showDialogueGenerico(){
    if(this !is AppCompatActivity){ return }
    DialogueGeneric.getInstance().showDialogueGenerico(supportFragmentManager,"DialogoGenerico")
}

fun ImageView.showImage(urlFoto : String? = null ) {
    if (urlFoto.isNullOrEmpty()) { return }
    Glide
        .with(context)
        .load(urlFoto)
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(R.drawable.ic_android_black_24dp)
        .skipMemoryCache(true)
        .centerCrop()
        .into(this)
}

fun isNetworkAvailable(context: Context): Boolean {
    try {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    } catch (e: Exception) {
        return false
    }
}

fun SettingPermision(context: Context, activity: Activity){
    try {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }catch (e: Exception){

    }
}

fun Context.showProgress(){
    if(this !is AppCompatActivity){
        return
    }else{
        runOnUiThread {
            ProgressBarPersonalized
                .getInstance()
                .show(supportFragmentManager,"progressbar")
        }

        GlobalScope.launch {
            delay(30_000)
            hiddenProgress()
        }
    }
}

fun Context.hiddenProgress(){
    if(this !is AppCompatActivity){
        return
    }else{
        runOnUiThread {
            ProgressBarPersonalized
                .getInstance()
                .dismiss()

        }
    }
}

fun String.isUserNameValid(username: String): Boolean {
    return if (username.contains('@')) {
        Patterns.EMAIL_ADDRESS.matcher(username).matches()
    } else { username.isNotBlank() }
}

fun String.isPasswordValid(password: String): Boolean {
    return password.length > 5
}

fun String.showToast() : String{
    Toast.makeText(App.mContext!!.applicationContext,this, Toast.LENGTH_LONG).show()
    return this
}

fun Context.crearDocumentoImagen() : File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName: String = "JPEG_" + timeStamp + "_"
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    if (!storageDir.exists()) storageDir.mkdirs()
    val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
    return imageFile
}

fun ImageView.mostrarImagen(
    url : String
){
    if (url.isNullOrEmpty()){ return }
    var cargoLaImagen = false
    Thread {

        try {
            Thread.sleep(500)
            post {
                Picasso
                    .get()
                    .load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.ic_android_black_24dp)
                    .error(R.drawable.ic_android_black_24dp)
                    .into(this,object : Callback {
                        override fun onSuccess() {
                            cargoLaImagen = true
                            Log.i("Picasso","Cargo Exitosa mente");
                        }

                        override fun onError(e: java.lang.Exception?) {
                            Log.e("Picasso","Fallo la carga de la imagen",e)
                        }

                    })
            }
            Thread.sleep(Endpoints.TiemposEspera.CARGA_IMAGENES.tiempo)

            if (cargoLaImagen){
                contadorIntentos = 0
                return@Thread
            }

            if (numeroIntentos == contadorIntentos){
                contadorIntentos = 0
                return@Thread
            }

            contadorIntentos ++
            mostrarImagen(url)
        } catch (e: java.lang.Exception) {
            Log.e("Error","",e)
            if(numeroIntentos == contadorIntentos){
                contadorIntentos = 0
                return@Thread
            }
            contadorIntentos ++
            mostrarImagen(url)
        }
    }.start()
}

fun Bitmap.convertirABase64() : String{
    val contenedorBitmap = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG,100,contenedorBitmap)
    return Base64.encodeToString(contenedorBitmap.toByteArray(), Base64.DEFAULT)
}

fun Bitmap.redimencionaUnaImagen(context : Context, rutaAncho : Int = R.dimen.dimen_100dp, rutaAlto : Int = R.dimen.dimen_100dp): Bitmap {

    val imagenRedimencionado = Bitmap.createScaledBitmap(
        this,
        context.resources.getDimension(rutaAncho).toInt(),
        context.resources.getDimension(rutaAlto).toInt(),
        false)
    return BitmapDrawable(context.resources, imagenRedimencionado).bitmap
}