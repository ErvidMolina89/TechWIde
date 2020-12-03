package com.widetech.mobile.wide_tech.Base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.DialogueGeneric
import com.widetech.mobile.wide_tech.Utils.showDialogueGenerico

open class BaseFragment: Fragment() {

    var baseActivity: BaseActivity? = null
    var vista: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        baseActivity = activity as BaseActivity
        return vista
    }


    fun dialogueFragment(title: String,
                         detail: String,
                         type: DialogueGeneric.TypeDialogue){
        DialogueGeneric
            .getInstance()
            .withTitle(title)
            .withText(detail)
            .withTypeDialogue(type)
            .withTextBtnAccept(R.string.btn_aceptar)
            .withActionBtnAccept {
                Log.e("","")
            }
        context?.showDialogueGenerico()
    }

}