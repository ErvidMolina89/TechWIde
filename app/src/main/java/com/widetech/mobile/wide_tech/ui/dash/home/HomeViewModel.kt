package com.widetech.mobile.wide_tech.ui.dash.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.Base.BaseActivity
import com.widetech.mobile.wide_tech.DataAccess.Repositories.RepoProducts
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.Products
import com.widetech.mobile.wide_tech.DataAccess.Repositories.RepoSynchronization
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.DialogueGeneric
import com.widetech.mobile.wide_tech.Utils.isNetworkAvailable
import com.widetech.mobile.wide_tech.Utils.showProgress

class HomeViewModel : ViewModel() {
    private val productsRepository = RepoProducts()
    private val synchronization = RepoSynchronization()
    var productsProfile: MutableLiveData<MutableList<Products>> = productsRepository.getListProdcuts()

    fun get_products() {
        productsRepository.CallService()
    }

    fun validateDB() {
        synchronization.getAllProducts(App.mContext!!).observeForever {
            if (it?.size != 0) {
                productsProfile.value = (it as MutableList<Products>)
            } else {
                callService()
            }
        }
    }

    private fun callService(){
        if(isNetworkAvailable(App.mContext!!)) {
            get_products()
        } else {
            BaseActivity().dialogue(
                App.mContext?.resources?.getString(R.string.Internet)!!,
                App.mContext?.resources?.getString(R.string.detail_falla_Internet)!!,
                DialogueGeneric.TypeDialogue.ADVERTENCIA)
        }
    }
}