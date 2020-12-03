package com.widetech.mobile.wide_tech.DataAccess.Repositories

import android.content.Context
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.DBWideTech
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RepoSynchronization {

    //Insert Models DB
    fun onInsertProducts(context: Context, usersList: MutableList<Products>, onSuccessInsert: (List<Long>) -> Unit = {}){
        GlobalScope.launch {
            onSuccessInsert(DBWideTech.getInstance(context).productDao().insertList(usersList))
        }
    }

    fun onInsertUser(context: Context, items: User, onSuccessInsert: (Long) -> Unit = {}){
        GlobalScope.launch {
            onSuccessInsert(DBWideTech.getInstance(context).userDao().insert(items))
        }
    }

    //Delete de Models DB
    fun deleteAllProducts(context: Context) =
        GlobalScope.launch {
            DBWideTech.getInstance(context).productDao().nukeProducts()
        }

    fun deleteAllUser(context: Context) =
        GlobalScope.launch {
            DBWideTech.getInstance(context).userDao().nukeUser()
        }

    //Metodos de consultas a DB
    fun getAllProducts(context: Context)
            = DBWideTech.getInstance(context).productDao().getProducts()

    fun getUserForUserID(context: Context, id: String)
            = DBWideTech.getInstance(context).userDao().getUserID(id)

    fun getAllUser(context: Context)
        = DBWideTech.getInstance(context).userDao().getUsers()
    }