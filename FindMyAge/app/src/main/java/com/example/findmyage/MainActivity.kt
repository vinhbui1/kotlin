package com.example.findmyage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun getyourage(view:View){
        val text:Int = editText.text.toString().toInt()
        val curentYear=Calendar.getInstance().get(Calendar.YEAR)
        val dob=curentYear - text
        textView.text=("tuoi cua ban $dob")
    }
}
