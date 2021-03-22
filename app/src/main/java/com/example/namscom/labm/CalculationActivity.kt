package com.example.namscom.labm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_calculation.*

class CalculationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)

        btnHome.setOnClickListener {
            // Homeページへ画面遷移
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra("VALUE", editText.text.toString())
            startActivity(intent)
        }
    }
}