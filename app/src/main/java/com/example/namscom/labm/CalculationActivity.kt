package com.example.namscom.labm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CalculationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)


        bntHome.setOnClickListener {
            // Homeページへ画面遷移
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra("VALUE", editText.text.toString())
            startActivity(intent)
        }
    }
}