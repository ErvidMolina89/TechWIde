package com.widetech.mobile.wide_tech.DataAccess.Connection.Resources

import com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Interfaces.IRetrofitParcelable
import com.google.gson.Gson

fun IRetrofitParcelable.ConvertirAObjeto(json: String): IRetrofitParcelable {
    val tmp = Gson().fromJson(json, this.javaClass)
    return tmp
}