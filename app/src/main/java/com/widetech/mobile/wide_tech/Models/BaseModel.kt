package com.widetech.mobile.wide_tech.Models

import com.google.gson.Gson
import com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Interfaces.IRetrofitParcelable
import java.io.Serializable

open class BaseModel :

    IRetrofitParcelable, Serializable {

    companion object {
        fun objectFromJson(json: String, type: Class<out BaseModel>): BaseModel? {
            try {
                val currentObject = Gson().fromJson(json, type)
                currentObject.doPostDeserializer()
                return currentObject
            } catch (e: com.google.gson.JsonSyntaxException) {
                return null
            }
        }
    }

    fun toJsonString(): String{
        return Gson().toJson(this)
    }

    open fun doPostDeserializer(){

    }
}