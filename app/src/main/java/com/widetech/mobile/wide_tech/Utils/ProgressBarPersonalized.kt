package com.widetech.mobile.wide_tech.Utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.widetech.mobile.wide_tech.R

class ProgressBarPersonalized private constructor() : DialogFragment() {

    companion object{
        private var stayVisible = false
        private var instance : ProgressBarPersonalized ?= null

        fun getInstance() : ProgressBarPersonalized{

            if(instance == null){
                instance = ProgressBarPersonalized()
            }

            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_progress,null,false)
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    override fun dismiss() {
        try {
            stayVisible = false
            if(fragmentManager == null ){
                return
            }
            super.dismiss()
            super.dismissAllowingStateLoss()
        } catch (e: Exception) {
            Log.d("Error","",e)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (stayVisible) return
            if(isAdded) return
            stayVisible = true
            super.show(manager, tag)
        } catch (e: Exception) {

        }
    }
}

