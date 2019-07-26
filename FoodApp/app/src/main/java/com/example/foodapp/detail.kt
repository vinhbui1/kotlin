package com.example.foodapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class detail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var bundle:Bundle=intent.extras
        var name =bundle.getString("name")
        var des =bundle.getString("des")
        var image=bundle.getInt("image")
        imView.setImageResource(image)
        tvName.text=name
        tvDes.text=des

    }

}
