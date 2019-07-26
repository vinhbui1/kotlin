package com.example.mynote

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.note.*

class addNote : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)




        try {
            var bundle:Bundle=intent.extras
            id=bundle.getInt("ID",0)
                etTitle.setText(bundle.getString("name"))
                etDes.setText(bundle.getString("des"))

        }catch (ex:Exception){}


    }
    var id=0
    fun buAdd(view: View){
        var dbManager =DbManager(this)

        var values = ContentValues()
        values.put("Title",etTitle.text.toString())
        values.put("Description",etDes.text.toString())
        if (id==0) {
            var ID = dbManager.Insert(values)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(this, "Cannot add", Toast.LENGTH_LONG).show()

            }
        }else{
            var selectionArgs = arrayOf(id.toString())
            var ID = dbManager.Update(values,"ID =?",selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note is updated", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(this, "Cannot updated", Toast.LENGTH_LONG).show()

            }

        }
        finish()
    }
}
