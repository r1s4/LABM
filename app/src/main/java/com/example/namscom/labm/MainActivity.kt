package com.example.namscom.labm

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mUserDao: UserDao
    lateinit var mAdapter: ArrayAdapter<String>

    private var mUserList: List<User> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUserDao = UserDatabase.getInstance(this).userDao()

        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListOf())
        list_view.adapter = mAdapter

        getUser()

        //+ボタンでの処理（ユーザの追加）
        btnAdd.setOnClickListener{

            //ダイアログ中のテキストエディタの設定
            val dialogEditText =  EditText(this)
            //文字数を15文字以下に設定
            val lengthFilter = InputFilter.LengthFilter(15)
            dialogEditText.filters = arrayOf(lengthFilter)

            AlertDialog.Builder(this)
                .setTitle("ユーザの追加")
                .setMessage("名前：")
                .setView(dialogEditText)
                .setPositiveButton("OK",{ dialog, which ->
                    val userText = dialogEditText.text.toString()
                    if (userText != null) {
                        insertUser(userText)
                    }
                })
                .show()
        }
    }

    private fun getUser() {
        mUserList = mUserDao.getAll()
        val userInfoList = mUserList.map { it.id.toString() + " . " + it.name}
        mAdapter.clear()
        mAdapter.addAll(userInfoList)
    }

    //名前追加の関数
    private fun insertUser(username:String) {
        val newUser = User(0, username, 0, dateandtime= 0, memo = "")
        mUserDao.insert(newUser)
        getUser()
    }

    //更新
    private fun updateUser() {
        if (mUserList.isEmpty()) return

        val editUser = mUserList.first()
        editUser.name = "更新されました"
        mUserDao.update(editUser)
        getUser()
    }

    //削除

    private fun deleteUser() {
        if (mUserList.isEmpty()) return

        val deleteUser = mUserList.first()
        mUserDao.delete(deleteUser)
        getUser()
    }
}