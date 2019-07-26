package com.example.zooapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.animal_ticker.view.*

class MainActivity : AppCompatActivity() {

    var listOfAnimals= ArrayList<Animal>()
    var adapter:AnimalAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load animal
        listOfAnimals.add(Animal("Badoon",
            "Badoon live in vietnam and very handsome",R.drawable.baboon,true))
        listOfAnimals.add(Animal("Bulldog",
            "Bulldog live in vietnam and very handsome",R.drawable.bulldog,false))
        listOfAnimals.add(Animal("Panda",
            "Panda live in vietnam and very handsome",R.drawable.panda,true))
        listOfAnimals.add(Animal("Bird",
            "Bird live in vietnam and very handsome",R.drawable.swallow_bird,false))
        listOfAnimals.add(Animal("Tiger",
            "Tiger live in vietnam and very handsome",R.drawable.white_tiger,true))
        listOfAnimals.add(Animal("Zebra",
            "Zebra live in vietnam and very handsome",R.drawable.zebra,false))
        adapter = AnimalAdapter(this,listOfAnimals)
        lvAnimal.adapter=adapter

    }

    class AnimalAdapter:BaseAdapter{
        var listOfAnimals= ArrayList<Animal>()
        var context: Context?=null
        constructor(context: Context,listOfAnimals: ArrayList<Animal>):super()
        {
            this.listOfAnimals=listOfAnimals
            this.context=context
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val animal = listOfAnimals[position]

            if (animal.isKiller==true){
                val myView = LayoutInflater.from(context).inflate(R.layout.animal_killer_ticket,null)
                myView.tvName.text=animal.name
                myView.tvDes.text=animal.des
                myView.imAnimal.setImageResource(animal.image!!)
                myView.setOnClickListener {
                    val intent=Intent(context,MainAnimal::class.java)
                    intent.putExtra("name",animal.name)
                    intent.putExtra("des",animal.des)
                    intent.putExtra("image",animal.image!!)
                    context!!.startActivity(intent)
                }
                return myView

            }
            else{
                val myView = LayoutInflater.from(context).inflate(R.layout.animal_ticker,null)
                myView.tvName.text=animal.name
                myView.tvDes.text=animal.des
                myView.imAnimal.setImageResource(animal.image!!)
                myView.setOnClickListener {
                    val intent=Intent(context,MainAnimal::class.java)
                    intent.putExtra("name",animal.name)
                    intent.putExtra("des",animal.des)
                    intent.putExtra("image",animal.image!!)
                    context!!.startActivity(intent)
                }
                return myView

            }



        }

        override fun getItem(position: Int): Any {
            return listOfAnimals[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfAnimals.size
        }


    }
}
