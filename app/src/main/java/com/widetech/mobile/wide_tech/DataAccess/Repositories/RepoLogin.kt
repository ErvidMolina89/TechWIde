package com.widetech.mobile.wide_tech.DataAccess.Repositories

import androidx.lifecycle.MutableLiveData
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.Base.BaseActivity
import com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Implementations.HandlerProxyRetrofitRx
import com.widetech.mobile.wide_tech.DataAccess.Connection.Resources.Services
import com.widetech.mobile.wide_tech.Models.Login
import com.widetech.mobile.wide_tech.Utils.DialogueGeneric

class RepoLogin {
    private var user : MutableLiveData<Login>? = getLogin()

    fun getLogin() : MutableLiveData<Login> {

        if (user == null ){
            val lgin = Login()
            lgin.Password = ""
            lgin.UserName = ""
            user = MutableLiveData()
            user?.value = lgin
        }
        return  user!!
    }

    fun CallService (_login: Login){
        HandlerProxyRetrofitRx(App.mContext!!)
            .withListenerAnswerObjects {
                user?.value = it as Login
            }
            .withListenerOfFailure { titulo, message ->
                BaseActivity().dialogue(
                    titulo.toString(),
                    message,
                    DialogueGeneric.TypeDialogue.ERROR
                )
            }
            .withMyClass(Login::class.java)
            .withMyService(Services.post_login)
            .withObjectSend(_login)
            .RunService()
    }
}