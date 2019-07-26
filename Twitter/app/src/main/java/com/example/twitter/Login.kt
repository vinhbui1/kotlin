package com.example.twitter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


public class Login : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()


    lateinit var storageReference: StorageReference
    lateinit var alertDialog: AlertDialog
    private var mAuth: FirebaseAuth? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var filepath:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Intit
        alertDialog= SpotsDialog.Builder().setContext(this).build()

        storageReference=FirebaseStorage.getInstance().getReference()

        mAuth = FirebaseAuth.getInstance()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        ivPerson.setOnClickListener(View.OnClickListener {
            loadImage()
        })
    }


    fun buLogin(view:View){
        LoginToFireebase(etEmail.text.toString(),etPassword.text.toString())

    }


    fun SplitString(email: String):String{
        val spilit=email.split("@")
        return spilit[0]
    }

val PICK_IMAGE_CODE=123

        fun loadImage(){
        var intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==PICK_IMAGE_CODE && data!=null && resultCode==Activity.RESULT_OK){

            filepath=data.data
            try{
                val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,filepath)
                ivPerson.setImageBitmap(bitmap)
            }catch (ex:Exception){}
            }
        }


//    var ACESSREADDATA=123
//    fun checkPermission(){
//        if (Build.VERSION.SDK_INT >=23){
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),ACESSREADDATA)
//                return
//            }
//
//        }
//        loadImage()
//
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        when(requestCode){
//            ACESSREADDATA->{
//                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    loadImage()
//
//                }
//                else{
//                    Toast.makeText(applicationContext,"Can not read your data", Toast.LENGTH_LONG).show()
//
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
    fun UploadImageToFirebase(){
    alertDialog.show()

    var currentUser=mAuth!!.currentUser
    val email:String=currentUser!!.email.toString()
    val df=SimpleDateFormat("ddMMyyHHmmss")
    val imageName=SplitString(email)+"_"+df.format(Date())
    var imagePath = FirebaseStorage.getInstance().getReference(imageName)//file name

    val uploadTask =imagePath!!.putFile(filepath!!)
    val task=uploadTask.continueWithTask{
            task ->
        if (!task.isSuccessful){
            Toast.makeText(applicationContext,"Upload failed",Toast.LENGTH_LONG).show()
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
            Toast.makeText(applicationContext,"Upload Successful",Toast.LENGTH_LONG).show()
            Log.d("DIRECTLINK",url)
            alertDialog.dismiss()
            myRef.child("User").child(currentUser.uid).child("email").setValue(currentUser.email)
            myRef.child("User").child(currentUser.uid).child("ProfileImage").setValue(url)
        }
    }



}

    fun LoginToFireebase(email:String,password:String){
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Authentication Success.", Toast.LENGTH_SHORT).show()
                    UploadImageToFirebase()
                    LoadTwitter()
                } else {

                    Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()

                }

            }
    }

    override fun onStart() {
        super.onStart()
        LoadTwitter()
    }

fun LoadTwitter(){
        var currentUser=mAuth!!.currentUser
        if(currentUser!=null){
            var intent=Intent(this,MainActivity::class.java)
            intent.putExtra("email",currentUser.email)
            intent.putExtra("Uid",currentUser.uid)
            startActivity(intent)
    }

}
}

