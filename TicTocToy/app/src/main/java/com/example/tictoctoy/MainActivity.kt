package com.example.tictoctoy

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.random
import kotlin.random.Random
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        var b:Bundle=intent.extras
        myEmail=b.getString("email")
        IncomingCall()
    }

    var myEmail:String?=null


    public fun buClick(view:View){
        val buselecTed= view as Button?
        var cellId = 0
        when(buselecTed!!.id){
            R.id.bu1 -> cellId=1
            R.id.bu2 -> cellId=2
            R.id.bu3 -> cellId=3
            R.id.bu4 -> cellId=4
            R.id.bu5 -> cellId=5
            R.id.bu6 -> cellId=6
            R.id.bu7 -> cellId=7
            R.id.bu8 -> cellId=8
            R.id.bu9 -> cellId=9
        }

        myRef.child("PlayerOnline").child(seionID.toString()).child(cellId.toString()).setValue(myEmail)
        WinNer(listPlayer1,listPlayer2)

    }

    var player = 1
    val listPlayer1 = ArrayList<Int>()
    val listPlayer2 = ArrayList<Int>()

    @SuppressLint("ResourceAsColor")
    fun player(buselecTed:Button, cellId:Int){

            if (player ==1)
            {
                buselecTed.text="X"
                buselecTed.setBackgroundResource(R.color.green)
                listPlayer1.add(cellId)
                player=2

            }
            else{
                buselecTed.text="O"
                buselecTed.setBackgroundResource(R.color.red)
                listPlayer2.add(cellId)
                player=1

            }
            buselecTed.isEnabled=false


    }

        var win = 0

     fun WinNer(listPlayer1:ArrayList<Int>,listPlayer2:ArrayList<Int>){
        if(listPlayer1.contains(1) && listPlayer1.contains(2)&&listPlayer1.contains(3))
        {
             win=1
        }
        if(listPlayer2.contains(1) && listPlayer2.contains(2)&&listPlayer2.contains(3))
        {
            win=2
        }
        if(listPlayer1.contains(4) && listPlayer1.contains(5)&&listPlayer1.contains(6))
        {
            win=1
        }
        if(listPlayer2.contains(4) && listPlayer2.contains(5)&&listPlayer2.contains(6))
        {
            win=2
        }
        if(listPlayer1.contains(7) && listPlayer1.contains(8)&&listPlayer1.contains(9))
        {
            win=1
        }
        if(listPlayer2.contains(7) && listPlayer2.contains(8)&&listPlayer2.contains(9))
        {
            win=2
        }
        if(listPlayer1.contains(1) && listPlayer1.contains(4)&&listPlayer1.contains(7))
        {
            win=1
        }
        if(listPlayer2.contains(1) && listPlayer2.contains(4)&&listPlayer2.contains(7))
        {
            win=2
        }
        if(listPlayer1.contains(2) && listPlayer1.contains(5)&&listPlayer1.contains(8))
        {
            win=1
        }
        if(listPlayer2.contains(2) && listPlayer2.contains(5)&&listPlayer2.contains(8))
        {
            win=2
        }
        if(listPlayer1.contains(3) && listPlayer1.contains(6)&&listPlayer1.contains(9))
        {
            win=1
        }
        if(listPlayer2.contains(3) && listPlayer2.contains(6)&&listPlayer2.contains(9))
        {
            win=2
        }

        //////
        if (win ==1){
            Toast.makeText(this,"Player 1 Win",Toast.LENGTH_LONG).show()
        }
        if (win ==2){
            Toast.makeText(this,"Player 2 Win",Toast.LENGTH_LONG).show()
        }
    }


    fun autoPlay(cellId:Int){



        val buselect :Button
        when(cellId){
            1->buselect=bu1
            2->buselect=bu2
            3->buselect=bu3
            4->buselect=bu4
            5->buselect=bu5
            6->buselect=bu6
            7->buselect=bu7
            8->buselect=bu8
            9->buselect=bu9

            else->buselect = bu2
        }
        player(buselect,cellId)




        }

    public fun buRequest(view:View){
        var userEmail =etName.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
        seionID=splitString(myEmail!!)+splitString(userEmail)
        playOnline(seionID!!)///husenjana
        playerSymbol="X"
    }

    public fun buAccest(view:View){
        var userEmail =etName.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
        seionID=splitString(userEmail)+splitString(myEmail!!)
        playOnline(seionID!!)
        playerSymbol="O"

    }
var number =0
    public fun IncomingCall(){

        myRef.child("Users").child(splitString(myEmail!!)).child("Request")
            .addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        listPlayer1.clear()
                        listPlayer2.clear()

                        val dt =dataSnapshot.value as HashMap<String,Any>
                        if (dt!=null)

                        {
                            var value:String

                            for (key in dt.keys)
                            {
                                value=dt[key] as String
                                etName.setText(value)

                                  val  notifyme= Notification()
                                notifyme.Notify(applicationContext,value+"want to play game ",number)
                                number++
                                myRef.child("Users").child(splitString(myEmail!!)).child("Request").setValue(true)
                                break
                            }
                        }



                    }catch (ex:Exception){}


                }

            })
    }

    fun splitString(str:String):String{
        var split = str.split("@")
        return split[0]
    }


    var seionID:String?=null
    var playerSymbol:String?=null


    fun playOnline(seionID:String){
        myRef.child("PlayerOnline").removeValue()
        myRef.child("PlayerOnline").child(seionID)
            .addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {

                        val dt =dataSnapshot.value as HashMap<String,Any>
                        if (dt!=null)

                        {
                            var value:String

                            for (key in dt.keys)
                            {
                                value=dt[key] as String
                               if (value!=myEmail){
                                   player=if(playerSymbol==="X") 1 else 2
                               }else{
                                   player=if(playerSymbol==="X") 2 else 1

                               }
                                autoPlay(key.toInt())
                            }
                        }



                    }catch (ex:Exception){}


                }

            })

    }
}
