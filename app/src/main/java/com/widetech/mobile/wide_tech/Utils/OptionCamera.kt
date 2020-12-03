package com.widetech.mobile.wide_tech.Utils

import android.annotation.SuppressLint
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.widetech.mobile.wide_tech.R
import kotlinx.android.synthetic.main.dialogue_selector_image.*


class OptionCamera(): DialogFragment()  {



    private var listener: AlertCameraOptions? = null

    @SuppressLint("ValidFragment")
    constructor(listener: AlertCameraOptions?) : this(){
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vista = inflater.inflate(R.layout.dialogue_selector_image, container, false)
        dialog.setCanceledOnTouchOutside(false)
        return vista


    }

    override fun onResume() {
        super.onResume()
        actionBtn()
    }

    interface AlertCameraOptions{
        fun callCameraApp()
        fun callGalleryApp()

    }
    fun actionBtn(){
        btn_camera_option.setOnClickListener {
            dismiss()
            listener!!.callCameraApp()
        }
        btn_gallery_option.setOnClickListener {
            dismiss()
            listener!!.callGalleryApp()
        }
        btnCancelOption.setOnClickListener { this.dismiss() }
    }
}