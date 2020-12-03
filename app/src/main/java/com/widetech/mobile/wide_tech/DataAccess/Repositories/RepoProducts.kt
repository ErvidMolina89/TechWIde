package com.widetech.mobile.wide_tech.DataAccess.Repositories

import androidx.lifecycle.MutableLiveData
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.Base.BaseActivity
import com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Implementations.HandlerProxyRetrofitRx
import com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Interfaces.IRetrofitParcelable
import com.widetech.mobile.wide_tech.DataAccess.Connection.Resources.Services
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.Products
import com.widetech.mobile.wide_tech.Utils.DialogueGeneric

class RepoProducts {
    private var products : MutableLiveData<MutableList<Products>>? = getListProdcuts()
    private val synchronization = RepoSynchronization()

    fun getListProdcuts() : MutableLiveData<MutableList<Products>> {

        if (products == null ){
            products = MutableLiveData()
            products?.value = emptyList<Products>().toMutableList()
        }
        return  products!!
    }

    fun CallService (){
        HandlerProxyRetrofitRx(App.mContext!!)
            .withListenerAnswerListObjectcs {
                products?.value = it as MutableList<Products>
                synchronization.onInsertProducts(App.mContext!!, it, ::onSuccessInsert)
            }
            .withListenerOfFailure { titulo, message ->
                BaseActivity().dialogue(
                    titulo.toString(),
                    message,
                    DialogueGeneric.TypeDialogue.ERROR
                )
            }
            .withMyClass(Products::class.java)
            .withMyService(Services.get_list_products)
            .withObjectSend(object : IRetrofitParcelable{})
            .RunService()
    }

    private fun onSuccessInsert(list: List<Long>) {
        val result: List<Long> = list
    }
}