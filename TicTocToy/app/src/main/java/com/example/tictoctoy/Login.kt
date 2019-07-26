package com.example.tictoctoy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.app.Application
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase




class Login : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference


    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
    }

    fun buLoginEvent(view: View){
        createAccountFisebase(etEmail.text.toString(),etPassword.text.toString())
        loadMain()

    }

    fun createAccountFisebase(email:String,password:String){
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText( applicationContext,"Authentication success.", Toast.LENGTH_SHORT).show()

                    val currentUser = mAuth!!.currentUser
                    if(currentUser!=null){
                        myRef.child("Users").child(splitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                    }
                        loadMain()


                } else {
                    Toast.makeText( applicationContext,"Authentication failed.", Toast.LENGTH_SHORT).show()
                }

                // ...
            }

    }

    public override fun onStart() {
        super.onStart()
        loadMain()
    }

    fun loadMain(){
        var currentUser=mAuth!!.currentUser
        if (currentUser!=null) {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser!!.email)
            intent.putExtra("uid", currentUser!!.uid)
            startActivity(intent)
        }
    }

    fun splitString(str:String):String{
        var split = str.split("@")
        return split[0]
    }
}
