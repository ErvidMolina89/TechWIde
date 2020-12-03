package com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.widetech.mobile.wide_tech.Models.BaseModel


@Entity
class User (

    @PrimaryKey
    @ColumnInfo(name = "Id")            var id          : Int? = null,
    @ColumnInfo(name = "Image")         var image       : String? = null,
    @ColumnInfo(name = "Name")          var name        : String? = null,
    @ColumnInfo(name = "LastName")      var lastName    : String? = null,
    @ColumnInfo(name = "Email")         var email       : String? = null,
    @ColumnInfo(name = "Firma")         var firma       : String? = null

): BaseModel()