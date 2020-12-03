package com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Implementations

import android.content.Context
import com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Interfaces.*
import com.widetech.mobile.wide_tech.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.ConnectException

class HandlerProxyRetrofitRx (private val context: Context) {

    //Escuchador objeto de respuesta
    private var listenerAnswerObject : ((IRetrofitParcelable?)->Unit)?= null
    fun withListenerAnswerObjects(listenerAnswerObject : ((IRetrofitParcelable?)->Unit)?) : HandlerProxyRetrofitRx{
        this.listenerAnswerObject =  listenerAnswerObject
        return this
    }

    //Escuchador de lista de objeto de respuesta
    private var listenerAnswerListObject : ((MutableList<IRetrofitParcelable>?)->Unit)?= null
    fun withListenerAnswerListObjectcs(listenerAnswerListObject : ((MutableList<IRetrofitParcelable>?)->Unit)?) : HandlerProxyRetrofitRx{
        this.listenerAnswerListObject = listenerAnswerListObject
        return this
    }

    //Escuchador de errores
    private var listenerOfFailure : ((Int,String)->Unit)?= null
    fun withListenerOfFailure(listenerOfFailure : ((Int,String)->Unit)?) : HandlerProxyRetrofitRx{
        this.listenerOfFailure = listenerOfFailure
        return this
    }

    //Clase de espera
    private var myClass : Class<*> ?= null
    fun withMyClass(myClass : Class<*>? ) : HandlerProxyRetrofitRx{
        this.myClass = myClass
        return this
    }

    //Servicio respuesta
    private var myService : IServiceParameters?= null
    fun withMyService(myService : IServiceParameters) : HandlerProxyRetrofitRx{
        this.myService = myService
        return this
    }

    //Objeto de envio o body
    private var myObjectSend : IRetrofitParcelable ?= null
    fun withObjectSend(myObjectSend : IRetrofitParcelable) : HandlerProxyRetrofitRx{
        this.myObjectSend = myObjectSend
        return this
    }

    //Ejecutar Servicio
    fun RunService() {
        ProxyRetrofitRx(context)
            .conlistenerAnswerSuccess{
                answerPositive(it!!)
            }
            .conListenerAnswerFailure {
                if (it is ConnectException){
                    listenerOfFailure?.invoke(R.string.Internet, context.resources.getString(R.string.detail_falla_Internet))
                }else listenerOfFailure?.invoke(it.hashCode(), it.message.toString())
            }
            .conHandlerCodeAnswer{
            }
            .withService(myService!!)
            .conObjectToSend(myObjectSend)
            .loadData()
    }

    // Respuesta positva del servicio
    private fun answerPositive(response: Any) {
        val json = Gson().toJson(response)
        if(generateSingleObject(json)){
            return
        }
        if(generateListObject(json)){
            return
        }

        listenerAnswerObject?.invoke(null)
        listenerAnswerListObject?.invoke(null)
    }

    //Generador de objeto unico respuesta
    private fun generateSingleObject(json : String) : Boolean{
        return try {
            val myObject = Gson().fromJson(json, myClass!!)
            listenerAnswerObject?.invoke(myObject as? IRetrofitParcelable)
            true
        }catch (e : Exception){
            false
        }
    }

    //Generador de lista de objetos en respuesta
    private fun generateListObject(json : String) : Boolean{
        return try {
            val myType = object : TypeToken<MutableList<Any>>(){}.type
            val myList = Gson().fromJson<Any>(json,myType) as MutableList<*>
            val castedList = emptyArray<Any>().toMutableList()
            for (item in myList){
                val json = Gson().toJson(item)
                val myObject = Gson().fromJson(json,myClass!!)
                castedList.add(myObject)
            }
            listenerAnswerListObject?.invoke(castedList as? MutableList<IRetrofitParcelable>)
            true
        }catch (e  : Exception){
            false
        }
    }
}