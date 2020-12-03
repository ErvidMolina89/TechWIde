package com.widetech.mobile.wide_tech.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.widetech.mobile.wide_tech.DataAccess.Repositories.RepoLogin
import com.widetech.mobile.wide_tech.Models.Login

class LoginViewModel : ViewModel() {

    private val loginRepository = RepoLogin()
    val loginResult : MutableLiveData<Login> = loginRepository.getLogin()

    fun login(_login: Login) {
        loginRepository.CallService(_login)
    }

}