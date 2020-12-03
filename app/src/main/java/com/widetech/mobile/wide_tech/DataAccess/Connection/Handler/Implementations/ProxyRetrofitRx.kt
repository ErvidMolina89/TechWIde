package com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Implementations

import android.content.Context
import com.widetech.mobile.wide_tech.DataAccess.Connection.Handler.Interfaces.*
import com.widetech.mobile.wide_tech.rest.Endpoints
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ProxyRetrofitRx (private val context: Context) {

    //Cargar Datos
    fun loadData(){

        val myCompositeDisposable = CompositeDisposable()
        val client = generateHeader()
        val rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

        //Define the Retrofit request//
        val requestInterface = Retrofit.Builder()
            .baseUrl(Endpoints.Url.URL_BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()

        //crear instancia de retrofit
        val implementacionDeInterface = requestInterface.create(GetData::class.java)

        //Agregar todos los disponible de RXJava a un CompositeDisponsable
        myCompositeDisposable.add(generateGetData(implementacionDeInterface)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(responseOfHandler())
        )
    }

    //generar Cabezera
    private fun generateHeader(): OkHttpClient {
        val httpClient = OkHttpClient.Builder().addInterceptor(object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val request = original.newBuilder()
                request.addHeader("autorization", "new_token")
                codeAnswer?.invoke(chain.proceed(request.build()).code())
                return  chain.proceed(request.build())
            }
        }).build()
        return httpClient
    }

    //generar servicio con url
    private fun generateGetData(requestInterface: GetData): Observable<Any> {
        return when(serviceSelected?.getMethods()) {
            IServiceParameters.Methods.GET -> requestInterface.getData(serviceSelected?.getURL()!! )
            IServiceParameters.Methods.POST -> requestInterface.postData(serviceSelected?.getURL()!!, objectToSend)
            IServiceParameters.Methods.PUT -> requestInterface.putData(serviceSelected?.getURL()!!, objectToSend)
            IServiceParameters.Methods.DELETE -> requestInterface.deleteData(serviceSelected?.getURL()!!, objectToSend)
            IServiceParameters.Methods.OPTIONS -> requestInterface.optionsData(serviceSelected?.getURL()!!, objectToSend)
            else -> requestInterface.getData(serviceSelected?.getURL()!!)
        }
    }

    //manejador de respuestas
    private fun responseOfHandler(): DisposableObserver<Any> {
        return object : DisposableObserver<Any>(){
            override fun onComplete() {

            }

            override fun onNext(response: Any) {
                listenerAnswerSuccess?.invoke(response)
            }

            override fun onError(error: Throwable) {
                listenerAnswerFailure?.invoke(error)
            }

        }
    }

    //Lector de codigos de respuesta
    private  var codeAnswer : ((Int?)->Unit) ?= null
    fun conHandlerCodeAnswer(code : ((Int?)->Unit)): ProxyRetrofitRx {
        this.codeAnswer = code
        return this
    }

    //Escuchador de respuesta exitosa
    private  var listenerAnswerSuccess : ((Any?)->Unit) ?= null
    fun conlistenerAnswerSuccess(responseRX:((Any?)->Unit)): ProxyRetrofitRx {
        this.listenerAnswerSuccess = responseRX
        return this
    }

    //Escuchador de respuesta de falla
    private  var listenerAnswerFailure : ((Throwable)->Unit) ?= null
    fun conListenerAnswerFailure(failure:((Throwable)->Unit)): ProxyRetrofitRx {
        this.listenerAnswerFailure = failure
        return this
    }

    //Servicio Seleccionado
    private var serviceSelected : IServiceParameters ?= null
    fun withService(service : IServiceParameters) : ProxyRetrofitRx{
        this.serviceSelected = service
        return this
    }

    //Objeto a Enviar
    private var objectToSend: IRetrofitParcelable? = null
    fun conObjectToSend(objet: IRetrofitParcelable?): ProxyRetrofitRx {
        this.objectToSend = objet
        return this
    }
}