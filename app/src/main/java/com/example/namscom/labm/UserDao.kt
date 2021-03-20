package com.example.namscom.labm

import androidx.room.*

//データベースにアクセスするメソッドを定義
@Dao
interface UserDao {
    //userの取得
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    //追加
    @Insert
    fun insert(user: User)

    //更新
    @Update
    fun update(user: User)

    //削除
    @Delete
    fun delete(user: User)
}