package com.example.namscom.labm

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calculation.*

class CalculationActivity : AppCompatActivity() {
    lateinit var mUserDao: UserDao
    lateinit var mAdapter: ArrayAdapter<String>

    private var mUserList: List<User> = listOf()

    var nStr : String = ""
    val nList = ArrayList<Double>() // arraylist to store numbers
    val oList = ArrayList<Char>() // arraylist to store operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)

        mUserDao = UserDatabase.getInstance(this).userDao()

        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListOf())

        getUser()

        num0.setOnClickListener {
            formula.text = "${formula.text}0"
            nStr += "0"
        }
        num1.setOnClickListener {
            formula.text = "${formula.text}1"
            nStr += "1"
        }
        num2.setOnClickListener {
            formula.text = "${formula.text}2"
            nStr += "2"
        }
        num3.setOnClickListener {
            formula.text = "${formula.text}3"
            nStr += "3"
        }
        num4.setOnClickListener {
            formula.text = "${formula.text}4"
            nStr += "4"
        }
        num5.setOnClickListener {
            formula.text = "${formula.text}5"
            nStr += "5"
        }
        num6.setOnClickListener {
            formula.text = "${formula.text}6"
            nStr += "6"
        }
        num7.setOnClickListener {
            formula.text = "${formula.text}7"
            nStr += "7"
        }
        num8.setOnClickListener {
            formula.text = "${formula.text}8"
            nStr += "8"
        }
        num9.setOnClickListener {
            formula.text = "${formula.text}9"
            nStr += "9"
        }
        point.setOnClickListener {
            formula.text = "${formula.text}."
            nStr += "."
        }
        equal.setOnClickListener {
            formula.text = "${formula.text}="
            addList(nStr)
            var result = calcualte().toString()
            formula.text = result
            nStr = result
            nList.clear()
            oList.clear()
        }
        add.setOnClickListener {
            formula.text = "${formula.text}+"
            addList(nStr,'+')
            nStr = ""
        }
        subtract.setOnClickListener {
            formula.text = "${formula.text}-"
            addList(nStr,'-')
            nStr = ""
        }
        multiply.setOnClickListener {
            formula.text = "${formula.text}*"
            addList(nStr,'*')
            nStr = ""
        }
        divide.setOnClickListener {
            formula.text = "${formula.text}/"
            addList(nStr,'/')
            nStr = ""
        }
        delete.setOnClickListener {
            var formulaStr = formula.text.toString()
            if (!formulaStr.isEmpty()) {
                formula.text = formulaStr.subSequence(0,formulaStr.lastIndex)
            }
            if (!nStr.isEmpty()) {
                nStr = nStr.substring(0, nStr.lastIndex)
            }
        }
        percent.setOnClickListener {
            formula.text = "${formula.text}%"
        }
        sign.setOnClickListener {

        }
        clear.setOnClickListener {
            formula.text = ""
            nStr = ""
            nList.clear()
            oList.clear()
        }

        //String?になってしまう
        var flag = intent.getStringExtra("MoneyFlag")
        var uname = intent.getStringExtra("UserName")

        if(flag!=null || uname!=null) {
            val money_flag=flag.toString()
            val user_name=uname.toString()
            //ENTERボタン
            enter.setOnClickListener {
                var title_text = ""


                //var total = formula.getText()
                var total = nStr
                var total_money=0

                if (money_flag == "0") {
                    title_text = "${total}円の貸し"
                } else if (money_flag == "1") {
                    title_text = "${total}円の借り"
                }
                AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                        .setTitle(title_text)
                        .setMessage("保存しますか？")
                        .setPositiveButton("保存", { dialog, which ->
                            // データを保存してホームに戻る
                            if (money_flag == "0") {
                                //貸す側なのでDBではプラス
                                total_money = total.toInt() + mUserDao.getFromNAME(user_name).total
                                //updateUser(user_name, total.toInt())
                            }else if(money_flag =="1"){
                                //借りる側なのでDB上ではマイナス
                                total_money = -(total.toInt()) +mUserDao.getFromNAME(user_name).total
                            }

                            println(total_money)

                            // HOMEページへ画面遷移
                            val intentsub = Intent(this, MainActivity::class.java)
                            intentsub.putExtra("TotalMoney", total_money.toString())
                            intentsub.putExtra("UserName",user_name)
                            setResult(Activity.RESULT_OK,intentsub)
                            finish()
                        })


                        //    val intent = Intent(this, MainActivity::class.java)
                        //    startActivity(intent)
                        //})
                        .setNegativeButton("いいえ", { dialog, which ->
                            // 何もせずに閉じる
                        })
                        .show()
            }   //end enter.setOnClickListener
        }

        //HOMEボタン
        btnHome.setOnClickListener {
            val user_name=uname.toString()
            var total_money=mUserDao.getFromNAME(user_name).total
            // Homeページへ画面遷移
            val intentsub = Intent(this, MainActivity::class.java)
            //nullを返さない用
            intentsub.putExtra("TotalMoney", total_money.toString());
            setResult(Activity.RESULT_OK,intentsub)
            finish()
        }

    } // end fun onCreate

    fun addList(str : String, ope : Char) {
        try {
            var num = str.toDouble()
            nList.add(num)
            oList.add(ope)
        }catch(e:Exception){
            formula.text = "Numeric error"
        }
    }

    fun addList(str : String) {
        try {
            var num = str.toDouble()
            nList.add(num)
        }catch(e:Exception){
            formula.text = "Numeric error"
        }
    }

    fun calcualte() : Double {

        var i = 0
        while (i < oList.size) {
            //do multiplication and division first
            if(oList.get(i) == '*' || oList.get(i) == '/') {
                var result = if (oList.get(i) == '*') nList.get(i) * nList.get(i+1) else nList.get(i) / nList.get(i+1)
                nList.set(i,result)
                nList.removeAt(i+1)
                oList.removeAt(i)
                i--
            }
            // change subtraction to addition
            else if(oList.get(i) == '-'){
                oList.set(i,'+')
                nList.set(i+1,nList.get(i+1) * -1)
            }
            i++
        }

        // get sum
        var result = 0.0
        for (i in nList){
            result += i
        }

        return result
    }// end fun calcualte

    private fun getUser() {
        mUserList = mUserDao.getAll()
        val userInfoList = mUserList.map {it.name}
        mAdapter.clear()
        mAdapter.addAll(userInfoList)
    }


} // end class
