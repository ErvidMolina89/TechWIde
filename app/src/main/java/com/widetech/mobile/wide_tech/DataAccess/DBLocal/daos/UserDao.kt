package com.widetech.mobile.wide_tech.DataAccess.DBLocal.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.User

@Dao
abstract class UserDao : BaseDao<User> {
    @Query("SELECT * FROM Products")
    abstract fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE id = :id")
    abstract fun getUserID(id: String): LiveData<User>

    @Query("DELETE FROM User")
    abstract fun nukeUser()
}