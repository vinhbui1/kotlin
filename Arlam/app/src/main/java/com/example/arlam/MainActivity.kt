package com.example.arlam

import android.app.FragmentManager
import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //button click to show TimePicker Dialog
        buSelec.setOnClickListener({
            val cal=Calendar.getInstance()
            val timeSetListerner= TimePickerDialog.OnTimeSetListener({
                view: TimePicker?, hourOfDay: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
                cal.set(Calendar.MINUTE,minute)

                tvTime.text=SimpleDateFormat("HH:mm").format(cal.time)
                val saveData=SaveData(this)
                saveData.setArlam(Calendar.HOUR_OF_DAY,Calendar.MINUTE)

            })
            TimePickerDialog(this,timeSetListerner,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show()
        })

    }


}
