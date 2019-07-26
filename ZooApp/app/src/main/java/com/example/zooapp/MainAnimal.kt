package com.example.zooapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_animal.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainAnimal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)
        var bundle:Bundle =intent.extras
        var name =bundle.getString("name")
        var des = bundle.getString("des")
        var image = bundle.getInt("image")
        imAnimal.setImageResource(image)
        tvName.text=name
        tvDes.text=des
    }
}