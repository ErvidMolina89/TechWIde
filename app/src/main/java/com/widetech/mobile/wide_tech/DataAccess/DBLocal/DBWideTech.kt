package com.widetech.mobile.wide_tech.DataAccess.DBLocal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.daos.*
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.*

@Database(entities = arrayOf(
    Products::class,
    User::class
), version = 1)
abstract class DBWideTech : RoomDatabase() {

    abstract fun productDao(): ProductsDao
    abstract fun userDao(): UserDao

    companion object {
        private const val nameDB = "WideTechDB"
        @Volatile
        private var INSTANCE: DBWideTech? = null

        fun getInstance(context: Context): DBWideTech =
                INSTANCE ?: synchronized(this) {
                    buildDatabase(context).also {
                        INSTANCE = it
                    }
                }

        private fun buildDatabase(context: Context) =
                Room
                    .databaseBuilder(context.applicationContext, DBWideTech::class.java, nameDB)
                    .build()
    }

}