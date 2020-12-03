package com.widetech.mobile.wide_tech.DataAccess.Connection.Resources

import com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Interfaces.IServiceParameters
import com.widetech.mobile.wide_tech.rest.Endpoints

enum class Services (url : String,
                     method : IServiceParameters.Methods)
    : IServiceParameters {

    post_login(Endpoints.Url.POST_LOGIN,IServiceParameters.Methods.POST),
    get_list_products(Endpoints.Url.GET_POST_PRODUCTS,IServiceParameters.Methods.POST)
    ;

    private val url : String
    private val method : IServiceParameters.Methods
    private var complement: String = ""

    init {
        this.url = url
        this.method = method
    }

    override fun getURL(): String {
        return url + complement
    }

    override fun getMethods(): IServiceParameters.Methods {
        return method
    }

    override fun getUrlWithComplement(complement: String): Services {
        this.complement = complement
        return this
    }
}