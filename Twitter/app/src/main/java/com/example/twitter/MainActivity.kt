package com.example.twitter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.text.TextUtils.split
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.*
import kotlinx.android.synthetic.main.ticket.view.*
import kotlinx.android.synthetic.main.twitter_ticket.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    var ListTwitter=ArrayList<Ticket>()
    var adapter:TwitterAdapter?=null
    private var filepath: Uri?=null
    lateinit var alertDialog: AlertDialog
    private var mAuth: FirebaseAuth? = null

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        alertDialog= SpotsDialog.Builder().setContext(this).build()
        ListTwitter.add(Ticket("0","him","URL","add"))


        adapter=TwitterAdapter(this,ListTwitter)
        lvTwitter.adapter=adapter
        loadPost()

    }


    inner class TwitterAdapter:BaseAdapter{
        var ListAdapter=ArrayList<Ticket>()
        var context:Context?=null
        constructor(context: Context,ListAdapter:ArrayList<Ticket>):super(){
            this.context=context
            this.ListAdapter=ListAdapter

        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var mytweet=ListAdapter[position]
        if(mytweet.twitterUserID.equals("add")){
            var myView=layoutInflater.inflate(R.layout.ticket,null)
            myView.ivPost.setOnClickListener {
                statusText=myView.tvDes.text.toString()
                UploadImageToFirebase()
            }
            myView.ivLoadImage.setOnClickListener(View.OnClickListener {
                loadImage()
            })

            return myView

        }
//        else if(mytweet.twitterUserID.equals("ads")){
//            var myView=layoutInflater.inflate(R.layout.ads_twitter,null)
//
//            var mAdView = myView.findViewById(R.id.adView) as AdView
//            val  adRequest = AdRequest.Builder().build()
//            mAdView.loadAd(adRequest)
//
//            return myView
//
//        }

        else{
            var myView=layoutInflater.inflate(R.layout.twitter_ticket,null)
            myView.txt_tweet.setText(mytweet.twitterText)
            Picasso.get().load(mytweet.twitterImageURL).into(myView.tweet_picture)

            myRef.child("User").child(mytweet.twitterUserID.toString()).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {

                        var td=dataSnapshot.value as HashMap<String,Any>
                        for(key in td.keys){
                            var user=td[key] as String
                            if (key.equals("ProfileImage")){
                                Picasso.get().load(user).into(myView.imAvarta)
                            }
                            else {
                                myView.txtUserName.setText(user)
                            }

                        }
                    }catch (ex:Exception){}
                }

            })




            return myView
        }

        }

        override fun getItem(position: Int): Any {
            return ListAdapter[0]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {

            return   ListAdapter.size
        }

    }
    var statusText:String ?=null
        val PICK_IMAGE_CODE=123
        fun loadImage(){
            var intent= Intent()
            intent.type="image/*"
            intent.action= Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==PICK_IMAGE_CODE && data!=null && resultCode== Activity.RESULT_OK){

            filepath=data.data
//            try{
//                val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,filepath)
//                ivPerson.setImageBitmap(bitmap)
//            }catch (ex: Exception){}
        }

    }
    fun UploadImageToFirebase(){
        alertDialog.show()

        var currentUser=mAuth!!.currentUser
        val email:String=currentUser!!.email.toString()
        val df= SimpleDateFormat("ddMMyyHHmmss")
        val imageName=SplitString(email)+"_"+df.format(Date())
        var imagePath = FirebaseStorage.getInstance().getReference(imageName)//file name
        val uploadTask =imagePath!!.putFile(filepath!!)
        val task=uploadTask.continueWithTask{
                task ->
            if (!task.isSuccessful){
                Toast.makeText(applicationContext,"Upload failed", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            }
            imagePath.downloadUrl
        }.addOnCompleteListener{
                task ->
            if (task.isSuccessful)
            {
                val dowloadUri=task.result!!
                val url =dowloadUri!!.toString()
                //.substring(0,dowloadUri.toString().indexOf("&token"))
                Toast.makeText(applicationContext,"Upload Successful", Toast.LENGTH_LONG).show()
                Log.d("DIRECTLINK",url)
                alertDialog.dismiss()

                myRef.child("Posts").push().setValue(User(currentUser.uid,statusText!!,url))
            }
        }



    }
   fun SplitString(email:String):String{
        var split = email.split("@")
       return split[0]
   }


    fun loadPost(){
        myRef.child("Posts").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    ListTwitter.clear()
                    ListTwitter.add(Ticket("0","him","URL","add"))
                    ListTwitter.add(Ticket("0","him","url","ads"))
                    var td=dataSnapshot.value as HashMap<String,Any>
                    for(key in td.keys){
                        var posts=td[key] as HashMap<String,Any>
                        ListTwitter.add(Ticket(key,
                            posts["status"] as String,
                            posts["url"] as String,
                            posts["uid"] as String))

                    }

                    adapter!!.notifyDataSetChanged()
                }catch (ex:Exception){}
            }

        })


    }

}