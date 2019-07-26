package com.example.foodapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.food_main.view.*

class MainActivity : AppCompatActivity() {

    var adapter:FoodAdapter?=null
    var foodArray= ArrayList<FoodApp>()
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            //load data

            foodArray.add(FoodApp("Coffee",
                "Coffee is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East:",R.drawable.coffee_pot))

            foodArray.add(FoodApp("Espresso",
                "Espresso is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East:",R.drawable.espresso))
            foodArray.add(FoodApp("French_fries",
                "French_fries is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East:",R.drawable.french_fries))
            foodArray.add(FoodApp("Honey",
                "Honey is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East:",R.drawable.honey))
            foodArray.add(FoodApp("Strawberry",
                "Strawberry is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East:",R.drawable.strawberry_ice_cream))
            foodArray.add(FoodApp("Sugger_cubes",
                "Sugger_cubes is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East:",R.drawable.sugar_cubes))
            adapter= FoodAdapter(this,foodArray)
            gvFood.adapter=adapter
        }


    class FoodAdapter:BaseAdapter{

        var foodArray= ArrayList<FoodApp>()
        var context:Context?=null
        constructor(context: Context,foodArray:ArrayList<FoodApp>):super(){
            this.context=context
            this.foodArray=foodArray
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
           var food =foodArray[position]
            var myView=LayoutInflater.from(context).inflate(R.layout.food_main,null)
            myView.tvname.text=food.name
            myView.imViewfood.setImageResource(food.image!!)
           myView.setOnClickListener({
               var intent=Intent(context,detail::class.java)
               intent.putExtra("name",food!!.name)
               intent.putExtra("des",food!!.des)
               intent.putExtra("image",food!!.image)
               context!!.startActivity(intent)

           })
            return myView

        }

        override fun getItem(position: Int): Any {
            return foodArray[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return foodArray.size
        }

    }
}
