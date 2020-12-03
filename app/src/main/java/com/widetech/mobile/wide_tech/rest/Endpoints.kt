package com.widetech.mobile.wide_tech.rest

object Endpoints {
    object Url {
        const val URL_BASE: String = "http://ws4.shareservice.co"
        const val POST_LOGIN: String = "/TestMobile/rest/Login"
        const val GET_POST_PRODUCTS: String = "/TestMobile/rest/GetProductsData"
    }
    object PutExtra {
        const val RC_SIGN_IN: Int = 550
    }

    enum class TiemposEspera(val tiempo: Long){
        SOLICITUD_SERVICIO(40000),
        ESPERA_HISTORIAL(500),
        CARGA_IMAGENES(5_000)
    }

    //keytool -keystore path-to-debug-or-production-keystore -list -v
    //340628073522-9qn5ko2kqijraobkrucuvjhujbldst4s.apps.googleusercontent.com
//    Alias: AndroidDebugKey
//    MD5: 29:D3:6E:05:B7:C7:5B:23:4E:EF:40:05:C7:E5:02:63
//    SHA1: 47:12:8F:03:0E:CB:CA:82:0A:30:78:7A:5D:D8:5E:4A:43:DC:BF:C8
//    SHA-256: 5C:C2:2B:F1:94:61:7B:63:F7:CA:14:89:30:EF:E3:4F:F7:67:DE:3E:33:EF:3E:3A:A5:DD:31:57:40:97:16:B4
//    Valid until: domingo 7 de febrero de 2049
}


