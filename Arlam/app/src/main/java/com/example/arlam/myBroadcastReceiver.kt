package com.example.arlam

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class myBroadcastReceiver:BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent!!.action.equals("com.tester.arlammanager")){
            var b=intent.extras
            Toast.makeText(context,b.getString("message"),Toast.LENGTH_LONG).show()

        }
    }

}