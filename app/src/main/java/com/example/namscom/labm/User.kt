package com.example.namscom.labm

import androidx.room.Entity
import androidx.room.PrimaryKey

//データベースのテーブルに相当
@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    var name: String,

    var total: Int,

    //型不明
    var dateandtime: Int,

    var memo: String

    //@ColumnInfo(name = "nenrei")
    //var age: Int
)
