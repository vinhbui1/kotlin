package com.example.mynote
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.note.view.*

class MainActivity : AppCompatActivity() {

    var listnodes=ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /// add data
       // listnodes.add(Note(1,"Milk coffe","Coffee is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East: A beverage "))
       // listnodes.add(Note(2,"Ice coffe","Coffee is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East: A beverage "))
       // listnodes.add(Note(3,"Become a fresher","Coffee is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain .... In 1583, Leonhard Rauwolf, a German physician, gave this description of coffee after returning from a ten-year trip to the Near East: A beverage "))



        /// load form DB
        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")

    }

    fun LoadQuery(title:String){
        var dbManager=DbManager(this)
        var projection= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.Query(projection,"Title like ?",selectionArgs,"Title")
        listnodes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID =cursor.getInt(cursor.getColumnIndex("ID"))
                val Title =cursor.getString(cursor.getColumnIndex("Title"))
                val Description =cursor.getString(cursor.getColumnIndex("Description"))
                listnodes.add(Note(ID,Title,Description))


            }while (cursor.moveToNext())

        }
        var myAdapter=myAdapter(this ,listnodes!!)
        lvNote.adapter=myAdapter

    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        /// search action
        var sv=menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        var sm=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
                LoadQuery("%"+query+"%")

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
            return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menuAdd ->{
                var intent=Intent(this,addNote::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    inner class myAdapter:BaseAdapter{

        var context:Context?=null
        var listnodesAdapter:ArrayList<Note>
        constructor( context:Context,listnodesAdapter:ArrayList<Note>):super(){
            this.listnodesAdapter=listnodesAdapter
            this.context=context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView=layoutInflater.inflate(R.layout.note,null)
            var myNote=listnodesAdapter[position]
            myView.tvTitle.text=myNote.noteName
            myView.tvDes.text=myNote.noteDes
            myView.ivDelete.setOnClickListener(View.OnClickListener {
                var dbManager=DbManager(this.context!!)
                var selectionArgs= arrayOf(myNote.noteId.toString())
                dbManager.Delete( "ID=?",selectionArgs)
                LoadQuery("%")


            })
            myView.ivEdit.setOnClickListener(View.OnClickListener {
                var intent=Intent(context,addNote::class.java)
                intent.putExtra("ID",myNote.noteId)
                intent.putExtra("name",myNote.noteName)
                intent.putExtra("des",myNote.noteDes   )
                startActivity(intent)

            })
            return myView
        }

        override fun getItem(position: Int): Any {
            return listnodesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listnodesAdapter.size
        }
    }


}
