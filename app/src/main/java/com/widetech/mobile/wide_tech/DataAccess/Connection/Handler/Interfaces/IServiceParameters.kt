package com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Interfaces

interface IServiceParameters {
    enum class Methods{
        GET,
        POST,
        PUT,
        DELETE,
        OPTIONS;
    }

    fun getURL() : String
    fun getMethods() : Methods
    fun getUrlWithComplement(complement: String = ""): IServiceParameters
}