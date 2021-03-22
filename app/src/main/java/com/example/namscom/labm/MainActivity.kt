package com.example.namscom.labm

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.ArrayAdapter
import android.widget.EditText
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
                .setPositiveButton("OK", { dialog, which ->
                    val userText = dialogEditText.text.toString()
                    if (userText != null) {
                        insertUser(userText)
                    }
                })
                .show()
        }

        if (list_view != null) {
            // listViewの行がクリックされた時のイベント
            list_view.setOnItemClickListener { parent, view, position, id ->
                // listViewのクリックされた行のテキストを取得
                val dialog_user_name = parent.getItemAtPosition(position) as String

                //貸すor借りるを計算に引き継ぐ
                var money_flag=0

                //DB上のお金のデータ（プラスなら貸してる，マイナスなら借りてる）
                var dialog_user_money=mUserDao.getFromNAME(dialog_user_name).total

                //表示するテキスト（タイトル）の内容
                var text_message =""
                //貸してるか借りてるかで表示が変わる
                if (dialog_user_money>=0) {
                    text_message="${dialog_user_name}さんに${dialog_user_money} 円の貸しです"
                } else if(dialog_user_money<0){
                    text_message="${dialog_user_name}さんに${-(dialog_user_money)} 円の借りです"
                }


                //ダイアログの表示
                val strList = arrayOf("貸す", "借りる")

                AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                        .setTitle(text_message)
                        .setItems(strList, { dialog, which ->
                            //貸す判定（0）か，借りる判定(1)か
                            if(which==0){
                                money_flag=0
                            }else if (which==1){
                                money_flag=1
                            }
                            // 計算ページへ画面遷移
                            val intent = Intent(this, CalculationActivity::class.java)
                            //intent.putExtra("VALUE", editText.text.toString())
                            startActivity(intent)

                        })
                        .setNeutralButton("削除", { dialog, which ->
                            //削除
                            deleteUser(dialog_user_name)
                        })
                        .setPositiveButton("閉じる", { dialog, which ->
                            // 何もせずに閉じる
                        })
                        .show()
            }
        }
    }

    private fun getUser() {
        mUserList = mUserDao.getAll()
        val userInfoList = mUserList.map {it.name}
        mAdapter.clear()
        mAdapter.addAll(userInfoList)
    }


    //名前追加の関数
    private fun insertUser(username: String) {
        val newUser = User(0, username, 0, dateandtime = 0, memo = "")
        mUserDao.insert(newUser)
        getUser()
    }


    //idから削除　（少しくどいので改良する）
    private fun deleteUser(username: String) {
        val deleteUser=mUserDao.getFromNAME(username)
        mUserDao.delete(deleteUser)
        getUser()
    }
}