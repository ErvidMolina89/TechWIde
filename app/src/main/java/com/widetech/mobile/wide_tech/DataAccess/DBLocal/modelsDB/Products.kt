package com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.widetech.mobile.wide_tech.Models.BaseModel


@Entity
class Products (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")            var id          : Int? = null,
    @ColumnInfo(name = "Description")   var Description : String? = null,
    @ColumnInfo(name = "Name")          var Name       : String? = null,
    @ColumnInfo(name = "ImageUrl")      var ImageUrl    : String? = null

): BaseModel()