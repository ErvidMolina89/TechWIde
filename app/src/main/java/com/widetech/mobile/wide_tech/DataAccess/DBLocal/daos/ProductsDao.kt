package com.widetech.mobile.wide_tech.DataAccess.DBLocal.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.Products

@Dao
abstract class ProductsDao : BaseDao<Products> {
    @Query("SELECT * FROM Products")
    abstract fun getProducts(): LiveData<List<Products>>

    @Query("SELECT * FROM Products WHERE id = :id")
    abstract fun getProductsID(id: Int): LiveData<Products>

    @Query("DELETE FROM Products")
    abstract fun nukeProducts()
}