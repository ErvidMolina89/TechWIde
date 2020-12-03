package com.widetech.mobile.wide_tech.ui.dash.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.Base.BaseFragment
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.Products
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.hiddenProgress
import com.widetech.mobile.wide_tech.Utils.showProgress
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var listProducts: MutableList<Products> = emptyList<Products>().toMutableList()
    private var adapterRecyclerView : HomeRecyclerViewAdapter ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_home, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        onStyleRecycler()
        listenerViewModel()
        homeViewModel.validateDB()
        baseActivity?.applicationContext?.showProgress()
    }

    private fun onStyleRecycler() {
        recyclerViewSearchResults!!.let {
            it.layoutManager = LinearLayoutManager(App.mContext)
            this@HomeFragment.adapterRecyclerView = HomeRecyclerViewAdapter(App.mContext!!, emptyList<Products>().toMutableList())
            recyclerViewSearchResults.adapter = this@HomeFragment.adapterRecyclerView
        }
    }

    private fun listenerViewModel(){
        homeViewModel.productsProfile.observeForever {
            listProducts = it
            if(listProducts.size != 0){
                recyclerViewSearchResults.post {
                    adapterRecyclerView?.setData(listProducts)
                    baseActivity?.applicationContext?.hiddenProgress()
                }
            }
        }

        editTextSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                adapterRecyclerView?.setData(filterListUser(s.toString()))
            }
        })
    }

    private fun filterListUser (valor: String) :MutableList<Products>{
        val list = listProducts.filter {
            return@filter it.Name?.toLowerCase()?.contains(valor.toLowerCase())!!
        }
        if(list.size ==  0){
            include_list_empty.visibility = View.VISIBLE
        } else{
            if(include_list_empty.visibility == View.VISIBLE){
                include_list_empty.visibility = View.GONE
            }
        }
        return list.toMutableList()
    }
}