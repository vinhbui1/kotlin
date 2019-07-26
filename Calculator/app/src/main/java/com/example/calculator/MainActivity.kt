package com.example.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buNumberEvent(view: View){

        if (Isop){
            Shownumber.setText("")
        }

        var buSelect = view as Button
        var NumberValues:String=Shownumber.text.toString()

        when(buSelect.id){
            bu0.id ->{
                NumberValues+="0"
            }
            bu1.id ->{
                NumberValues+="1"
            }
            bu2.id ->{
                NumberValues+="2"
            }
            bu3.id ->{
                NumberValues+="3"
            }
            bu4.id ->{
                NumberValues+="4"
            }
            bu5.id ->{
                NumberValues+="5"
            }
            bu6.id ->{
                NumberValues+="6"
            }
            bu7.id ->{
                NumberValues+="7"
            }
            bu8.id ->{
                NumberValues+="8"
            }
            bu9.id ->{
                NumberValues+="9"
            }
            buDot.id ->{
                ////to do preven to much .dot
                if (NumberValues.contains(".")){
                    NumberValues=NumberValues
                }
                else
                NumberValues+="."
            }
            buPlusMinus.id ->{
                if (NumberValues.contains("-")){
                    NumberValues=NumberValues
                }
                else
                NumberValues= "-" +NumberValues
            }
        }
        Shownumber.setText(NumberValues)
        Isop = false
    }


var  op ="*"
var Isop = true
var oldnumber=""


    fun Calculator(view: View)
    {


        var buSelec = view as Button
        when(buSelec.id) {


            buPlus.id -> {
                op="+"
            }

            bulMinus.id -> {
                op="-"
            }

            buMul.id -> {
                op="*"
            }

            buDiv.id -> {
                op="/"
            }

        }
        Isop = true
         oldnumber=Shownumber.text.toString()

    }

    fun equalOp(view: View){

        var newNumber:String=Shownumber.text.toString()
        var finalNumber:Double ?=null
        when(op){

            "+" -> {
                finalNumber =newNumber.toDouble()+oldnumber.toDouble()
            }
            "-" -> {
                finalNumber =oldnumber.toDouble()-newNumber.toDouble()
            }
            "*" -> {
                finalNumber =newNumber.toDouble()*oldnumber.toDouble()
            }
            "/" -> {
                finalNumber =oldnumber.toDouble()/newNumber.toDouble()
            }
        }

        Shownumber.setText(finalNumber.toString())

    }
    fun PersenOP(view: View){
        var persen:Double=Shownumber.text.toString().toDouble()/100
        Shownumber.setText(persen.toString())
        Isop = true

    }

    fun AClear(view: View){
        Shownumber.setText("0")
        Isop = true

    }
}
